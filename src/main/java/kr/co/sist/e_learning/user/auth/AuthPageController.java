package kr.co.sist.e_learning.user.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthPageController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtAuthUtils jwtAuthUtils;

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "user/sign_up/sign_up";
    }


    @GetMapping("/forgot-username")
    public String showForgotUsername() {
        return "user/login/forgot-username";
    }

    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "user/login/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPassword() {
        return "user/login/reset-password";
    }


    @GetMapping("/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // ① refreshToken 삭제 처리
        String refreshToken = jwtAuthUtils.extractRefreshTokenFromCookies(request);
        if (refreshToken != null) {
            authService.logout(refreshToken); // DB나 Redis에서 삭제
        }

        // ② accessToken, refreshToken 쿠키 삭제
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

        // ✅ 추가: JSESSIONID 쿠키도 삭제 (Spring이 내부적으로 생성한 세션 제거)
        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        // ✅ 추가: 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // ✅ Spring Security 컨텍스트 초기화
        SecurityContextHolder.clearContext();

        return "redirect:/";
    }


    @GetMapping("/social_signup")
    public String showSocialSignUpPage(HttpSession session, Model model) { // HttpSession 파라미터 추가
        String socialProvider = (String) session.getAttribute("socialProvider");
        String socialId = (String) session.getAttribute("socialId");
        String email = (String) session.getAttribute("email");

        // 세션에서 가져온 정보 모델에 추가
        model.addAttribute("socialProvider", socialProvider);
        model.addAttribute("socialId", socialId);
        if (email != null) {
            model.addAttribute("email", email);
        }

        // 세션에서 정보 제거 (일회성 사용)
        session.removeAttribute("socialProvider");
        session.removeAttribute("socialId");
        session.removeAttribute("email");

        return "user/sign_up/social_sign_up";
    }
    
    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request, Model model) {
        try {
            if (jwtAuthUtils.isLoggedIn(request)) {
                return "redirect:/";
            }
        } catch (Exception e) {
            // 예외 발생 시 로그인 페이지로 이동
            model.addAttribute("error", "로그인 상태 확인 중 오류 발생");
        }
        return "user/login/login";
    }
}
