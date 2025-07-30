package kr.co.sist.e_learning.mypage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private MyPageService mpSV;

    @Autowired
    private FundingService fdSV;

    @Value("${file.upload-dir.root}")
    private String uploadDirRoot;

    @Value("${upload.path.profile}")
    private String uploadPathWeb;
    
    
    private long getOrInitUserSeq(Authentication auth) {
        Object raw = auth.getPrincipal();
        Long userSeq = null;
        if (raw instanceof Long) {
            userSeq = (Long) raw;
        }
        return userSeq;
    }

    @GetMapping
    public String mypageMain(@RequestParam(value = "tab", required = false, defaultValue = "dashboard") String tab,
                             Authentication auth, Model model) {
        long userSeq = getOrInitUserSeq(auth);

        switch (tab) {
            case "my_info":
                model.addAttribute("myData", mpSV.getUserInfo(userSeq));
                break;
            case "lecture_history":
                model.addAttribute("lectureList", mpSV.getLectureHistory(userSeq));
                break;
            case "subscriptions":
                List<SubscriptionDTO> subs = mpSV.getSubscriptions(userSeq);
                model.addAttribute("subscriptionList", subs);
                break;
            case "dashboard":
            default:
                model.addAttribute("myData", mpSV.getMyPageData(userSeq));
                break;
        }

        model.addAttribute("currentTab", tab);
        return "mypage/mypage_main";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        long userSeq = getOrInitUserSeq(auth);
        model.addAttribute("myData", mpSV.getMyPageData(userSeq));
        return "mypage/dashboard";
    }

    @GetMapping("/lecture_history")
    public String lectureHistory(Authentication auth, Model model) {
        long userSeq = getOrInitUserSeq(auth);
        model.addAttribute("lectureList", mpSV.getLectureHistory(userSeq));
        model.addAttribute("myLectureList", mpSV.selectMyLectures(userSeq));
        return "mypage/lecture_history";
    }

    @PostMapping("/unsubscribe")
    public boolean cancelSubscription(HttpSession session, @RequestParam Long instructorId) {
        Long userSeq = (Long) session.getAttribute("user_seq");
        return mpSV.cancelSubscription(userSeq, instructorId);
    }

    @GetMapping("/my_info")
    public String myInfo(Authentication auth, Model model) {
        long userSeq = getOrInitUserSeq(auth);
        model.addAttribute("myData", mpSV.getUserInfo(userSeq));
        return "mypage/my_info";
    }


@PostMapping("/upload_profile")
@ResponseBody
public Map<String, Object> uploadProfile(@RequestParam("file") MultipartFile file, Authentication auth) {
    Map<String, Object> result = new HashMap<>();

    Object raw = auth.getPrincipal();
    if (!(raw instanceof Long userSeq) || file.isEmpty()) {
        result.put("success", false);
        result.put("message", "사용자 정보 또는 파일이 없습니다.");
        return result;
    }

    try {
        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
        if (!List.of("png", "jpg", "jpeg", "gif").contains(ext)) {
            result.put("success", false);
            result.put("message", "이미지 파일만 업로드 가능합니다.");
            return result;
        }

        String filename = "profile_" + userSeq + "." + ext;
        File uploadFolder = new File(uploadDirRoot + "/userprofile");
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        File dest = new File(uploadFolder, filename);
        file.transferTo(dest);

        // DB에는 웹 접근용 경로 저장
        String dbPath = uploadPathWeb + "/" + filename;
        mpSV.updateUserProfile(userSeq, dbPath);

        result.put("success", true);
        result.put("newPath", dbPath);
    } catch (Exception e) {
        e.printStackTrace();
        result.put("success", false);
        result.put("message", "업로드 중 오류 발생");
    }

    return result;
}

    @PostMapping("/delete_profile")
    @ResponseBody
    public Map<String, Object> deleteProfile(Authentication auth) {
        Map<String, Object> result = new HashMap<>();
        Object raw = auth.getPrincipal();
        if (!(raw instanceof Long userSeq)) {
            result.put("success", false);
            result.put("message", "사용자 정보 없음");
            return result;
        }

        try {
            String profilePath = mpSV.selectProfilePath(userSeq);
            if (profilePath != null && !profilePath.isBlank()) {
                File file = new File(uploadDirRoot + "/userprofile", profilePath.replace("/userprofile/", ""));
                if (file.exists()) {
                    file.delete();
                }
            }

            mpSV.updateUserProfile(userSeq, null);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "삭제 중 오류 발생");
        }

        return result;
    }

    @GetMapping("/reset_password")
    public String resetPassword() {
        return "mypage/reset_password";
    }

    @GetMapping("/link_account")
    public String linkAccount() {
        return "mypage/link_account";
    }

    @GetMapping("/leave")
    public String leave() {
        return "mypage/leave";
    }

    @GetMapping("/subscriptions")
    public String subscriptionPage(Authentication auth, Model model) {
        long userSeq = getOrInitUserSeq(auth);
        List<SubscriptionDTO> subscriptions = mpSV.getSubscriptions(userSeq);
        model.addAttribute("subscriptionList", subscriptions);
        return "mypage/subscriptions";
    }

    @GetMapping("/wallet")
    public String accountPage(Model model, Authentication auth) {
        long userSeq = getOrInitUserSeq(auth);
        FundingDTO accountInfo = fdSV.getAccountInfo(userSeq);
        model.addAttribute("accountInfo", accountInfo);
        return "mypage/wallet";
    }

    @GetMapping("/donation")
    public String fundingPage(Model model, Authentication auth) {
        long userSeq = getOrInitUserSeq(auth);
        List<FundingDTO> fundingList = fdSV.getUserFundings(userSeq);
        model.addAttribute("fundingList", fundingList);
        model.addAttribute("donationType", "given");
        return "mypage/donation";
    }

    @GetMapping("/user_header")
    public String userHeader() {
        return "mypage/user_header";
    }

    @GetMapping("/sidebar")
    public String sidebarPage() {
        return "mypage/sidebar";
    }
}
