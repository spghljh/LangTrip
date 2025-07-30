package kr.co.sist.e_learning.adminDash;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminDashController {

    @Autowired
    @Qualifier("adminDashServiceImpl")
    private AdminDashService adSV;

    // 페이지 진입용
    @GetMapping("/adminDash/user_statistics")
    public String userStatisticsPage() {
        return "adminDash/user_statistics"; 
    }

    // AJAX 데이터 요청
    @ResponseBody
    @GetMapping("/admin/api/user_stats")
    public Map<String, Object> getUserStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("dailySignup", adSV.getDailySignupStats());
        stats.put("signupPath", adSV.getSignupPathStats());
        stats.put("unsignReason", adSV.getUnsignReasonStats());
        return stats;
    }
}