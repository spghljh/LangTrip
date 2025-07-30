package kr.co.sist.e_learning.admin.signup;

import java.util.List;
import java.util.Map;


public interface AdminSignupDAO {
    void insertVerificationCode(EmailVerificationDTO dto);
    EmailVerificationDTO selectValidVerification(String email, String code);
    void markCodeVerified(String verificationSeq);
    void insertSignupRequest(AdminSignupDTO dto);
    boolean isDuplicateId(String adminId);
    List<String> selectRoleCodesByDept(String dept);
    void insertSignupPermission(Map<String, String> param);

    
}
