package kr.co.sist.e_learning.user.auth;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtAuthUtils jwtAuthUtils;

    @Autowired
    private UserRepository userRepository; // UserRepository 주입

    /**
     * 이메일 인증 코드 전송
     */
    @PostMapping("/email/send")
    public ResponseEntity<?> sendEmailVerification(@RequestBody EmailRequestDTO dto) {
        String verificationSeq = authService.sendEmailVerification(dto.getEmail());
        return ResponseEntity.ok(
                new SimpleResponseDTO(true, "이메일 인증 코드 발송 완료", verificationSeq)
        );
    }

    /**
     * 이메일 인증 코드 검증
     */
    @PostMapping("/email/verify")
    public ResponseEntity<?> verifyEmailCode(@RequestBody EmailCodeVerifyRequestDTO dto) {
        boolean isValid = authService.verifyEmailCode(
                dto.getVerificationSeq(),
                dto.getCode()
        );
        return ResponseEntity.ok(
                new SimpleResponseDTO(isValid,
                        isValid ? "인증 성공" : "인증 실패",
                        null)
        );
    }

    /**
     * 이메일 중복 체크
     */
    @GetMapping("/email/check")
    public ResponseEntity<?> checkEmailDuplicate(@RequestParam String email) {
        boolean duplicated = authService.isEmailDuplicated(email);
        return ResponseEntity.ok(
                new SimpleResponseDTO(!duplicated,
                        duplicated ? "이미 가입된 이메일입니다." : "사용 가능한 이메일입니다.",
                        null)
        );
    }

    /**
     * 닉네임 중복 체크
     */
    @GetMapping("/nickname/check")
    public ResponseEntity<?> checkNicknameDuplicate(@RequestParam String nickname) {
        boolean duplicated = authService.isNicknameDuplicated(nickname);
        return ResponseEntity.ok(
                new SimpleResponseDTO(!duplicated,
                        duplicated ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다.",
                        null)
        );
    }

    @PostMapping("/nickname/find")
    public ResponseEntity<?> findNicknameByEmail(@RequestBody EmailRequestDTO dto) {
        String nickname = authService.findNicknameByEmail(dto.getEmail());
        if (nickname != null) {
            return ResponseEntity.ok(new SimpleResponseDTO(true, "닉네임을 찾았습니다.", nickname));
        } else {
            return ResponseEntity.status(404).body(new SimpleResponseDTO(false, "해당 이메일로 가입된 닉네임이 없습니다.", null));
        }
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO dto) {
        boolean success = authService.forgotPassword(dto);
        if (success) {
            return ResponseEntity.ok(new SimpleResponseDTO(true, "임시 비밀번호가 이메일로 발송되었습니다.", null));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new SimpleResponseDTO(false, "입력하신 정보와 일치하는 사용자가 없습니다.", null));
        }
    }
    
    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody LocalSignUpRequestDTO dto) {
        authService.signup(dto);
        return ResponseEntity.ok(
                new SimpleResponseDTO(true, "회원가입 완료", null)
        );
    }

    @PostMapping("/socialSignup")
    public ResponseEntity<?> socialSignup(@RequestBody SocialSignUpRequestDTO dto,
                                          HttpServletResponse response) {
        authService.socialSignup(dto, response);
        return ResponseEntity.ok(
                new SimpleResponseDTO(true, "소셜 회원가입 완료", null)
        );
    }

    /**
     * 로컬 로그인
     */
    @PostMapping("/login/local")
    public ResponseEntity<?> localLogin(@RequestBody LocalLoginRequestDTO dto,
                                        HttpServletResponse response) {
        try {
            String loginStatus = authService.localLogin(dto, response);
            if ("FORCE_CHANGE_PASSWORD".equals(loginStatus)) {
                return ResponseEntity.ok(new SimpleResponseDTO(true, "임시 비밀번호로 로그인했습니다. 비밀번호를 변경해야 합니다.", "FORCE_CHANGE_PASSWORD"));
            } else {
                return ResponseEntity.ok(new SimpleResponseDTO(true, "로컬 로그인 성공", null));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SimpleResponseDTO(false, e.getMessage(), null));
        }
    }

    @PostMapping("/login/social")
    public ResponseEntity<?> socialLogin(@RequestBody SocialLoginDTO dto,
                                         HttpServletResponse response) {
        authService.socialLogin(dto, response);
        return ResponseEntity.ok(
                new SimpleResponseDTO(true, "소셜 로그인 성공", null)
        );
    }

    /**
     * Access Token 재발급
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            authService.reissueAccessToken(request, response);
            return ResponseEntity.ok(new SimpleResponseDTO(true, "Access Token 재발급 완료", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new SimpleResponseDTO(false, e.getMessage(), null));
        }
    }
    
    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO dto,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        try {
            authService.resetPassword(dto.getUserId(), dto.getNewPassword());

            // ✅ 쿠키 삭제
            Cookie accessCookie = new Cookie("accessToken", null);
            accessCookie.setMaxAge(0);
            accessCookie.setPath("/");
            accessCookie.setHttpOnly(true);
            response.addCookie(accessCookie);

            Cookie refreshCookie = new Cookie("refreshToken", null);
            refreshCookie.setMaxAge(0);
            refreshCookie.setPath("/");
            refreshCookie.setHttpOnly(true);
            response.addCookie(refreshCookie);

            // ✅ 세션 무효화
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // ✅ 응답: 프론트에게 리디렉션 지시
            return ResponseEntity.ok(new SimpleResponseDTO(true, "비밀번호가 성공적으로 변경되어 로그아웃됩니다.", "/login"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new SimpleResponseDTO(false, e.getMessage(), null));
        }
    }

    

    @GetMapping("/user/current-id")
    public ResponseEntity<?> getCurrentUserId(HttpServletRequest request) {
        try {
            String userId = jwtAuthUtils.getUserIdFromToken(request);
            return ResponseEntity.ok(new SimpleResponseDTO(true, "현재 사용자 ID 조회 성공", userId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SimpleResponseDTO(false, e.getMessage(), null));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getLoginStatus(Authentication authentication) {
        if (authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication.getPrincipal() instanceof String &&
              authentication.getPrincipal().equals("anonymousUser"))) {

            try {
                Long userSeq = Long.parseLong(authentication.getPrincipal().toString());
                return userRepository.findByUserSeq(userSeq)
                        .map(user -> ResponseEntity.ok(Map.of(
                            "loggedIn", true,
                            "nickname", user.getNickname()
                        )))
                        .orElseGet(() -> ResponseEntity.ok(Map.of("loggedIn", false)));
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "Invalid user ID in token"));
            }
        }
        return ResponseEntity.ok(Map.of("loggedIn", false));
    }

}
    
    
