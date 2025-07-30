package kr.co.sist.e_learning.admin.auth;


import kr.co.sist.e_learning.common.aop.Loggable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @GetMapping("/login")
    public String loginForm() {
        return "admin/login/login";
    }

    @GetMapping("/logout")
    @Loggable(actionType = "ADMIN_LOGOUT")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.getPrincipal() instanceof AdminUserDetails) {
            AdminUserDetails adminUserDetails = (AdminUserDetails) authentication.getPrincipal();
            model.addAttribute("adminRoles", adminUserDetails.getAuthorities().stream()
                                            .map(grantedAuthority -> grantedAuthority.getAuthority())
                                            .collect(Collectors.toList()));
        }
        return "/adminDash/user_statistics";
    }
}

