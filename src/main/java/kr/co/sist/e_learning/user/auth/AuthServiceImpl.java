package kr.co.sist.e_learning.user.auth;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private JwtAuthUtils jwtAuthUtils;

    @Autowired
    private PasswordHistoryRepository passwordHistoryRepository;

    private String generateRandomCode() {
        SecureRandom rand = new SecureRandom();
        int code = 100000 + rand.nextInt(900000);
        return String.valueOf(code);
    }

    @Override
    public String sendEmailVerification(String email) {
        String code = generateRandomCode();
        String verificationSeq = UUID.randomUUID().toString();

        EmailVerificationEntity entity = new EmailVerificationEntity();
        entity.setVerificationSeq(verificationSeq);
        entity.setEmail(email);
        entity.setCode(code);
        entity.setStatus("SENT");
        entity.setCreatedAt(Timestamp.from(Instant.now()));
        entity.setExpiresAt(Timestamp.from(Instant.now().plusSeconds(300)));

        emailVerificationRepository.save(entity);

        

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[LangTrip] 이메일 인증 코드");
            helper.setText(
                    "<h2>인증 코드: " + code + "</h2>"
                            + "<p>5분 안에 입력하세요.</p>"
                    , true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IllegalStateException("이메일 발송에 실패했습니다.");
        }

        return verificationSeq;
    }

    @Override
    public boolean verifyEmailCode(String verificationSeq, String code) {
        EmailVerificationEntity entity =
                emailVerificationRepository.findByVerificationSeq(verificationSeq);

        if (entity == null) {
            return false;
        }

        boolean isValid = entity.getCode().equals(code)
                && entity.getExpiresAt().after(Timestamp.from(Instant.now()))
                && "SENT".equals(entity.getStatus());

        if (isValid) {
            entity.setStatus("VERIFIED");
            emailVerificationRepository.save(entity);
        }

        return isValid;
    }

    @Override
    public boolean isEmailDuplicated(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isNicknameDuplicated(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public String findNicknameByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null) {
            String nickname = user.getNickname();
            if (nickname == null || nickname.isEmpty()) {
                return null;
            }
            int length = nickname.length();
            if (length > 3) {
                return nickname.substring(0, 3) + "*".repeat(length - 3);
            } else {
                return nickname.substring(0, 1) + "*".repeat(length - 1);
            }
        }
        return null;
    }

    @Override
    public void signup(LocalSignUpRequestDTO dto) {
        if (dto.getPassword().contains(" ")) {
            throw new IllegalArgumentException("비밀번호에는 공백을 포함할 수 없습니다.");
        }
        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setSocialProvider("LOCAL");
        user.setSignupPath(dto.getSignUp_path());
        this.createUser(user); 
    }

    @Override
    public void socialSignup(SocialSignUpRequestDTO sDTO, HttpServletResponse response) {
        UserEntity user = new UserEntity();
        user.setNickname(sDTO.getNickname());
        user.setSocialId(sDTO.getSocialId());
        user.setSocialProvider(sDTO.getSocialProvider());
        user.setSignupPath(sDTO.getSignUp_path());

        this.createUser(user); 
        
        

        String token = jwtTokenProvider.createAccessToken(user.getUserId(), user.getUserSeq());
        setTokenCookie(response, token);
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        String userId;
        do {
            userId = UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 10)
                    .toUpperCase();
        } while (userRepository.existsByUserId(userId));
        
        user.setUserId(userId);

        if (user.getProfile() == null) {
            user.setProfile("/images/default_profile.png");
        }

        if (user.getStatus() == null) {
            user.setStatus("ACTIVE");
        }

        if (user.getPasswordStatus() == null) {
            user.setPasswordStatus("ACTIVE");
        }

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        if ("LOCAL".equals(user.getSocialProvider()) && user.getSocialId() == null) {
            user.setSocialId(null);
        }
        
        


        return userRepository.save(user);
    }

    @Override
    public void saveRefreshToken(String userId, String refreshToken, LocalDateTime expiresAt) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
      
        entity.setUserId(userId); // userId 설정
        entity.setRefreshToken(refreshToken);
        entity.setExpiresAt(expiresAt);
        entity.setCreatedAt(LocalDateTime.now());

   
        refreshTokenRepository.save(entity);
    }

    @Override
    public String localLogin(LocalLoginRequestDTO dto, HttpServletResponse response) {
        UserEntity user = userRepository.findByNickname(dto.getNickname());
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getUserSeq());
        String refreshToken = jwtTokenProvider.createRefreshToken();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
        saveRefreshToken(user.getUserId(), refreshToken, expiresAt);

        setTokenCookie(response, accessToken);
        setRefreshTokenCookie(response, refreshToken);

        if ("TEMP".equals(user.getPasswordStatus())) {
            return "FORCE_CHANGE_PASSWORD";
        }

        return "SUCCESS";
    }

    private void setTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 개발환경에서는 false
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60);
        response.addCookie(cookie);
    }
    
    private void setRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 개발환경에서는 false
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(cookie);
    }

    
    @Override
    public void socialLogin(SocialLoginDTO dto, HttpServletResponse response) {
        // 소셜 로그인 로직 예시 (provider, socialId 기준으로 조회)
        UserEntity user = userRepository
                .findBySocialProviderAndSocialId(dto.getSocialProvider(), dto.getSocialId())
                .orElseThrow(() -> new RuntimeException("소셜 회원가입이 필요합니다."));

        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getUserSeq());

        // Refresh Token 발급
        String refreshToken = jwtTokenProvider.createRefreshToken();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
        saveRefreshToken(user.getUserId(), refreshToken, expiresAt);

        // 쿠키 저장
        setTokenCookie(response, accessToken);
        setRefreshTokenCookie(response, refreshToken);
    }
    
    @Override
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }
    
    @Override
    public Long reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtAuthUtils.extractRefreshTokenFromCookies(request);

        if (refreshToken == null) {
            throw new RuntimeException("Refresh Token 없음");
        }

        RefreshTokenEntity token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh Token 만료 혹은 없음"));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh Token 만료");
        }

        String userId = token.getUserId(); // refresh token entity에 포함됨
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("Refresh Token에 해당하는 사용자를 찾을 수 없습니다.");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getUserSeq());
        jwtAuthUtils.setAccessTokenCookie(response, newAccessToken);
        return user.getUserSeq();
    }

    @Override
    public boolean forgotPassword(ForgotPasswordRequestDTO dto) {
        UserEntity user = userRepository.findByNickname(dto.getNickname());
        if (user == null || !Objects.equals(user.getEmail(), dto.getEmail())) {
            return false; // 사용자 정보 불일치
        }

        // 현재 비밀번호가 임시 비밀번호가 아닌 경우에만 히스토리에 저장
        if (!"TEMP".equals(user.getPasswordStatus())) {
            PasswordHistoryEntity newHistory = new PasswordHistoryEntity();
            newHistory.setUser(user);
            newHistory.setPasswordHash(user.getPassword()); // 현재 비밀번호를 히스토리에 저장
            newHistory.setCreatedAt(LocalDateTime.now());
            passwordHistoryRepository.save(newHistory);
        }

        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setPasswordStatus("TEMP");

        // Ensure socialId is a valid Long for local users before saving
        if ("LOCAL".equals(user.getSocialProvider())) {
            if (user.getSocialId() == null) {
                user.setSocialId(null);
            } else {
                // String 타입으로 변경되었으므로 Long.parseLong 대신 String 그대로 사용
                // 또는 필요에 따라 다른 유효성 검사 로직 추가
            }
        }

        userRepository.save(user);
        

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(dto.getEmail());
            helper.setSubject("[LangTrip] 임시 비밀번호 발급");
            helper.setText(
                    "<h2>임시 비밀번호: " + tempPassword + "</h2>"
                            + "<p>로그인 후 반드시 비밀번호를 변경해주세요.</p>"
                    , true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // 이메일 발송 실패
        }
        return true;
    }

    @Override
    public void resetPassword(String userId, String newPassword) {
        if (newPassword.contains(" ")) {
            throw new IllegalArgumentException("비밀번호에는 공백을 포함할 수 없습니다.");
        }

        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        // 새 비밀번호가 현재 비밀번호와 동일한지 확인
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new RuntimeException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
        }

        List<PasswordHistoryEntity> passwordHistories = passwordHistoryRepository.findByUser_UserSeq(user.getUserSeq());
        for (PasswordHistoryEntity history : passwordHistories) {
            if (passwordEncoder.matches(newPassword, history.getPasswordHash())) {
                throw new RuntimeException("이전에 사용했던 비밀번호입니다.");
            }
        }

        // 현재 비밀번호가 임시 비밀번호가 아닐 경우에만 히스토리에 저장
        if (!"TEMP".equals(user.getPasswordStatus())) {
            PasswordHistoryEntity newHistory = new PasswordHistoryEntity();
            newHistory.setUser(user);
            newHistory.setPasswordHash(user.getPassword()); // 현재 비밀번호를 히스토리에 저장
            newHistory.setCreatedAt(LocalDateTime.now());
            passwordHistoryRepository.save(newHistory);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordStatus("ACTIVE");
        userRepository.save(user);
    }
}
    
    

 
    	
