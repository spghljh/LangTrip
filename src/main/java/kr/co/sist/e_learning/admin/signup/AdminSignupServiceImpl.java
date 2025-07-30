package kr.co.sist.e_learning.admin.signup;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


@Transactional
@Service
public class AdminSignupServiceImpl implements AdminSignupService {

	@Autowired
	private JavaMailSender mailSender;

    @Autowired
    private AdminSignupDAO signupDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void sendVerificationCode(String email) {
        String code = generateRandomCode();
        EmailVerificationDTO dto = new EmailVerificationDTO();
        dto.setVerificationSeq(UUID.randomUUID().toString());
        dto.setEmail(email);
        dto.setCode(code);
        dto.setStatus("SENT");
        dto.setCreatedAt(Timestamp.from(Instant.now()));
        dto.setExpiresAt(Timestamp.from(Instant.now().plusSeconds(300))); // 5분 유효

        signupDAO.insertVerificationCode(dto);

        // TODO: 이메일 발송 구현 필요
        System.out.println("임시 인증 코드: " + code);
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[EduPro] 관리자 인증 코드");
            helper.setText("<h2>인증 코드: " + code + "</h2><p>5분 안에 입력하세요.</p>", true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IllegalStateException("이메일 발송에 실패했습니다.");
        }
    }

    @Override
    public boolean verifyCode(String email, String code) {
        EmailVerificationDTO dto = signupDAO.selectValidVerification(email, code);
        if (dto != null) {
            signupDAO.markCodeVerified(dto.getVerificationSeq());
            return true;
        }
        return false;
    }

    @Override
    public void registerAdmin(AdminSignupDTO dto) {
        dto.setRequestId(UUID.randomUUID().toString());

        String hash = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(hash);

        signupDAO.insertSignupRequest(dto);

        // ✅ DB에서 부서별 권한 조회
        List<String> roleCodes = signupDAO.selectRoleCodesByDept(dto.getDept());

        // ✅ 각 권한 insert
        for (String roleCode : roleCodes) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("requestId", dto.getRequestId());
            param.put("roleCode", roleCode);
            signupDAO.insertSignupPermission(param);
        }
    }


    private String generateRandomCode() {
        SecureRandom rand = new SecureRandom();
        int code = 100000 + rand.nextInt(900000);
        return String.valueOf(code);
    }
    

    @Override
    public boolean isDuplicateId(String adminId) {
        return signupDAO.isDuplicateId(adminId);
    }

}
