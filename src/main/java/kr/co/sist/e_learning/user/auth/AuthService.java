package kr.co.sist.e_learning.user.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    String sendEmailVerification(String email);

    boolean verifyEmailCode(String verificationSeq, String code);

    boolean isEmailDuplicated(String email);

    boolean isNicknameDuplicated(String nickname);

    String findNicknameByEmail(String email);

    void signup(LocalSignUpRequestDTO dto);

    void socialSignup(SocialSignUpRequestDTO dto, HttpServletResponse response);

    String localLogin(LocalLoginRequestDTO dto, HttpServletResponse response);

    void socialLogin(SocialLoginDTO dto, HttpServletResponse response);

    UserEntity createUser(UserEntity user);

    void saveRefreshToken(String userId, String refreshToken, java.time.LocalDateTime expiresAt);
    
    void logout(String refreshToken);
    
    Long reissueAccessToken(HttpServletRequest request, HttpServletResponse response);

    boolean forgotPassword(ForgotPasswordRequestDTO dto);

    void resetPassword(String userId, String newPassword);
    
    

}
