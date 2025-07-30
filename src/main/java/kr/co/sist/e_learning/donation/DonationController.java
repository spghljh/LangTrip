package kr.co.sist.e_learning.donation;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DonationController {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private DonationService donationService;

    @GetMapping("/user/donation")
    public String donationPage(@RequestParam("lectureNo") String lectureNo,
                               Authentication authentication,
                               Model model) {
        System.out.println("[LOG] donationPage 메서드 진입 lectureNo=" + lectureNo);

        LectureDetailDTO lecture = lectureService.getLectureDetail(lectureNo);
        if (lecture == null) {
            // error/404 템플릿 없으면 임시로 메인페이지로 리다이렉트
            return "redirect:/";
        }

        Object principal = authentication.getPrincipal();
        Long userSeq = null;

        if (principal instanceof Long) {
            userSeq = (Long) principal;
        } else if (principal instanceof String) {
            try {
                userSeq = Long.valueOf((String) principal);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Principal String 값을 Long으로 변환할 수 없습니다.");
            }
        } else {
            // 예를 들어 UserDetails 구현체인 경우 username 이용하여 userSeq 조회하는 로직 추가 필요
            throw new IllegalStateException("지원하지 않는 Principal 타입: " + principal.getClass());
        }

        Long userMiles = donationService.getUserMiles(userSeq);
        System.out.println("[DEBUG] userSeq = " + userSeq);
        System.out.println("[DEBUG] userMiles = " + userMiles);

        model.addAttribute("lecture", lecture);
        model.addAttribute("userMiles", userMiles);

        System.out.println("lectureNo = " + lectureNo);
        System.out.println("lecture = " + lecture);
        System.out.println("userSeq = " + userSeq);
        System.out.println("auth = " + authentication);

        return "user/donation/donation";
    }

    

    @GetMapping("/donation/success")
    public String donationSuccess(@RequestParam String lectureTitle,
                                  @RequestParam String instructorName,
                                  @RequestParam int amount,
                                  @RequestParam(required = false) String message,
                                  Model model) {
        DonationDTO donation = new DonationDTO(lectureTitle, instructorName, amount, message);
        model.addAttribute("donation", donation);
        return "user/donation/donation_success";
    }
}
