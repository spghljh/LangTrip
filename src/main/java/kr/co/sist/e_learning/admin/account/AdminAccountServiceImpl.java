package kr.co.sist.e_learning.admin.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.sist.e_learning.common.aop.Loggable;
import kr.co.sist.e_learning.admin.signup.AdminSignupDAO;

import java.util.*;
import kr.co.sist.e_learning.common.service.EmailService;

@Service
public class AdminAccountServiceImpl implements AdminAccountService {

    @Autowired
    private AdminAccountDAO dao;
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AdminSignupDAO adminSignupDAO;

    @Override
    public List<AdminAccountUnifiedDTO> getUnifiedAdminList(Map<String, Object> params) {
        List<AdminAccountUnifiedDTO> list = dao.selectUnifiedAdminList(params);

        // ✅ 권한 정보 채워넣기
        for (AdminAccountUnifiedDTO dto : list) {
            List<String> roles;

            if ("PENDING".equals(dto.getStatus())) {
                roles = dao.selectRolesByRequestId(dto.getRequestId());
            } else {
                roles = dao.selectRolesByAdminId(dto.getAdminId());
            }

            dto.setRoleCodes(roles != null ? roles : Collections.emptyList());
        }

        return list;
    }

    @Override
    public List<AdminAccountUnifiedDTO> getUnifiedAdminList(String status, String searchType, String searchKeyword, String sort) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        params.put("sort", sort);
        return getUnifiedAdminList(params); // ✅ 핵심 로직 재사용
    }

    @Override
    public int getUnifiedAdminTotalCount(Map<String, Object> params) {
        return dao.countUnifiedAdmins(params);
    }

    @Override
    @Transactional
    public void updateAdmin(AdminAccountUnifiedDTO dto) {
        // Fetch the existing admin details to compare department
        AdminAccountUnifiedDTO existingAdmin = dao.selectByAdminId(dto.getAdminId());

        // Update personal details (name, email, phone, inline, status)
        dao.updateAdmin(dto);

        // Check if department has changed and update roles accordingly
        if (existingAdmin != null && !Objects.equals(existingAdmin.getDept(), dto.getDept())) {
            // Department has changed, fetch new roles based on the new department
            List<String> newRoleCodes = adminSignupDAO.selectRoleCodesByDept(dto.getDept());
            dto.setRoleCodes(newRoleCodes);
        }

        // Update roles if they are provided in the DTO (either from frontend or department change)
        List<String> roleCodes = dto.getRoleCodes();
        if (roleCodes != null) { // Only update if roleCodes are explicitly set
            dao.deleteAdminRoles(dto.getAdminId());
            if (!roleCodes.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("adminId", dto.getAdminId());
                map.put("roleCodes", roleCodes);
                dao.insertAdminRoles(map);
            }
        }
    }

    @Override
    public AdminAccountUnifiedDTO getById(String id) {
        AdminAccountUnifiedDTO dto = dao.selectUnifiedById(id);
        if (dto == null) {
            throw new IllegalArgumentException("관리자 또는 가입신청 정보가 존재하지 않음: " + id);
        }

        if (dto.getRequestId() != null) {
            List<String> roles = dao.selectRolesByRequestId(dto.getRequestId());
            dto.setRoleCodes(roles != null ? roles : Collections.emptyList());
        } else if (dto.getAdminId() != null) {
            List<String> roles = dao.selectRolesByAdminId(dto.getAdminId());
            dto.setRoleCodes(roles != null ? roles : Collections.emptyList());
        } else {
            dto.setRoleCodes(Collections.emptyList());
        }

        return dto;
    }

    @Override
    @Transactional
    @Loggable(actionType = "SIGNUP_APPROVE")
    public void approveSignup(String requestId) {
        AdminAccountUnifiedDTO dto = dao.selectByRequestId(requestId);
        if (dto == null || !"PENDING".equals(dto.getStatus())) {
            throw new IllegalStateException("가입신청 승인 불가 상태");
        }

        List<String> roles = dao.selectRolesByRequestId(requestId);
        dto.setRoleCodes(roles != null ? roles : Collections.emptyList());

        dao.insertAdmin(dto);

        if (!dto.getRoleCodes().isEmpty()) {
            Map<String, Object> param = new HashMap<>();
            param.put("adminId", dto.getAdminId());
            param.put("roleCodes", dto.getRoleCodes());
            dao.insertAdminRoles(param);
        }

        dao.updateSignupStatus(requestId, "APPROVED");

        // ✅ 승인 메일 전송
        String html = """
            <h3>[가입 승인 안내]</h3>
            <p>안녕하세요, 관리자 계정 가입 요청이 승인되었습니다.</p>
            <p><strong>ID:</strong> %s<br><strong>이름:</strong> %s</p>
            <p>이제 시스템에 로그인하실 수 있습니다.</p>
        """.formatted(dto.getAdminId(), dto.getAdminName());

        emailService.sendEmail(dto.getEmail(), "[SIST] 관리자 가입 승인 안내", html);
    }

    @Override
    @Loggable(actionType = "SIGNUP_REJECT")
    public void rejectSignup(String requestId, String reason) {
    	dao.updateSignupStatusWithReason(requestId, "REJECTED", reason);

        AdminAccountUnifiedDTO dto = dao.selectByRequestId(requestId);
        String email = dto.getEmail();

        String html = """
            <h3>[가입 거절 안내]</h3>
            <p>안녕하세요, 관리자 가입 요청이 거절되었습니다.</p>
            <p><strong>사유:</strong> %s</p>
            <p>문의가 있으시면 관리자에게 연락 주세요.</p>
        """.formatted(reason);

        emailService.sendEmail(email, "[SIST] 관리자 가입 요청 거절 안내", html);
    }
    
    @Override
    public List<String> getAllDepts() {
        return dao.selectDistinctDepts();
    }
    
    

}
