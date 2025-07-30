package kr.co.sist.e_learning.admin.donation;

import kr.co.sist.e_learning.admin.PageRequestDTO_donation;
import kr.co.sist.e_learning.admin.PageResponseDTO_donation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/donation")
@RequiredArgsConstructor
@Log4j2
public class AdminDonationController {

    private final AdminDonationService adminDonationService;

    @GetMapping
    public String donationList(Model model,
                               DonationSearchDTO searchDTO,
                               @ModelAttribute PageRequestDTO_donation pageRequestDTO) {
        
        PageResponseDTO_donation<DonationVO> responseDTO = adminDonationService.getDonationList(searchDTO, pageRequestDTO);
        model.addAttribute("pageResponse", responseDTO);
        model.addAttribute("searchDTO", searchDTO);
        model.addAttribute("totalDonations", responseDTO.getTotalCnt()); // For displaying total count

        
        return "admin/donation/admin_donation";
    }

    @PostMapping("/deleteMessage/{donationId}")
    @ResponseBody
    public ResponseEntity<String> deleteMessage(@PathVariable String donationId) {
        log.info("Received request to delete message for donationId: {}", donationId);

        boolean success = adminDonationService.deleteDonationMessage(donationId);
        if (success) {
            log.info("Message deleted successfully for donationId: {}", donationId);
            return ResponseEntity.ok("Message deleted successfully");
        } else {
            log.warn("Failed to delete message for donationId: {}", donationId);
            return ResponseEntity.badRequest().body("Failed to delete message");
        }
    }
}