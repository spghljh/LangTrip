package kr.co.sist.e_learning.admin.signup;


public interface AdminSignupService {

    // 이메일 인증 요청
    void sendVerificationCode(String email);

    // 이메일 인증 확인
    boolean verifyCode(String email, String code);

    // 회원가입 요청 저장
    void registerAdmin(AdminSignupDTO dto);
    
    boolean isDuplicateId(String adminId);


}
