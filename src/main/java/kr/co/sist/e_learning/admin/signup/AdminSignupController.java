package kr.co.sist.e_learning.admin.signup;

import kr.co.sist.e_learning.common.aop.Loggable;
import kr.co.sist.e_learning.common.aop.Loggable;
import kr.co.sist.e_learning.common.aop.Loggable;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class AdminSignupController {

    @Autowired
    private AdminSignupService signupService;

    // 이메일 인증 코드 발송
    @PostMapping("/send-code")
    @ResponseBody
    @Loggable(actionType = "SIGNUP_SEND_CODE")
    public void sendVerification(@RequestParam String email) {
        signupService.sendVerificationCode(email);
        System.out.println("씨삐라빨삐라ㅃ이ㅏ러ㅣ아ㅓ리ㅏㄹ");
    }

    // 인증코드 확인
    @PostMapping("/verify-code")
    @ResponseBody
    @Loggable(actionType = "SIGNUP_VERIFY_CODE")
    public String verify(@RequestParam String email, @RequestParam String code) {
        boolean result = signupService.verifyCode(email, code);
        return result ? "success" : "fail";
    }

    // 회원가입 요청 저장
    @PostMapping("/signup")
    @ResponseBody
    @Loggable(actionType = "SIGNUP_REQUEST")
    public void signupProcess(@RequestBody AdminSignupDTO dto) {
        dto.setRequestId(UUID.randomUUID().toString()); // 트리거가 없어도 유니크하게 생성
        signupService.registerAdmin(dto);
    }
    
    // 회원가입 요청 저장
    @GetMapping("/signup")
    @Loggable(actionType = "SIGNUP_PAGE_VIEW")
    public String signup() {
    	return "admin/signup/signup";
    }
    
    @GetMapping("/check-id")
    @ResponseBody
    @Loggable(actionType = "SIGNUP_CHECK_ID")
    public String checkAdminId(@RequestParam("adminId") String adminId) {
        boolean exists = signupService.isDuplicateId(adminId);
        return exists ? "exists" : "ok";
    }
    
}
