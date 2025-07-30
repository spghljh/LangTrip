package kr.co.sist.e_learning.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        UserDTO loginUser = userService.login(email, password);
        
        if (loginUser != null) {
            session.setAttribute("user_seq", loginUser.getUserSeq());  // ✅ 세션에 user_seq 저장
            return "redirect:/mypage/dashboard";
        } else {
            model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "login"; // 다시 로그인 폼으로
        }
    }
}

