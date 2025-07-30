package kr.co.sist.e_learning.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kr.co.sist.e_learning.user.auth.SimpleResponseDTO;
import kr.co.sist.e_learning.mypage.FundingService; // FundingService import 추가
import kr.co.sist.e_learning.mypage.FundingDTO; // FundingDTO import 추가
import java.util.List; // List import 추가

@RestController
@RequestMapping("/api/donation")
public class DonationRestController {

    private static final Logger logger = LoggerFactory.getLogger(DonationRestController.class);

    @Autowired
    private DonationService donationService;

    @Autowired // FundingService 주입 추가
    private FundingService fundingService;

    @PostMapping
    public ResponseEntity<?> donate(@RequestBody DonationRequestDTO dto, Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                logger.warn("Authentication failed: User is not authenticated.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new SimpleResponseDTO(false, "사용자가 인증되지 않았습니다.", null));
            }

            Object principal = authentication.getPrincipal();
            Long userSeq;

            // 타입을 안전하게 처리하도록 개선
            if (principal instanceof Long) {
                userSeq = (Long) principal;
            } else if (principal instanceof String) {
                userSeq = Long.valueOf((String) principal);
            } else {
                logger.error("Unsupported Principal Type: {}", principal.getClass());
                throw new IllegalStateException("지원하지 않는 Principal 타입: " + principal.getClass());
            }

            logger.info("Donation request received from user with ID: {}", userSeq);

            // 후원 처리
            DonationDTO donationResult = donationService.donate(userSeq, dto);

            if (donationResult == null) {
                logger.error("Donation processing failed for user ID: {}", userSeq);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SimpleResponseDTO(false, "후원 처리 실패", null));
            }

            logger.info("Donation processed successfully for user ID: {}", userSeq);
            return ResponseEntity.ok(donationResult);

        } catch (IllegalStateException e) {
            logger.error("Invalid Principal Type: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new SimpleResponseDTO(false, "유효하지 않은 인증 정보입니다.", null));
        } catch (Exception e) {
            logger.error("Error occurred during donation process: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new SimpleResponseDTO(false, "후원 처리 중 오류가 발생했습니다.", null));
        }
    }

    @GetMapping("/donations") // 새로운 GET 엔드포인트 추가
    public ResponseEntity<?> getDonations(@RequestParam("type") String donationType, Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                logger.warn("Authentication failed: User is not authenticated.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new SimpleResponseDTO(false, "사용자가 인증되지 않았습니다.", null));
            }

            Object principal = authentication.getPrincipal();
            Long userSeq;

            if (principal instanceof Long) {
                userSeq = (Long) principal;
            } else if (principal instanceof String) {
                userSeq = Long.valueOf((String) principal);
            } else {
                logger.error("Unsupported Principal Type: {}", principal.getClass());
                throw new IllegalStateException("지원하지 않는 Principal 타입: " + principal.getClass());
            }

            List<FundingDTO> donations;
            if ("given".equals(donationType)) {
                donations = fundingService.getUserFundings(userSeq);
            } else if ("received".equals(donationType)) {
                donations = fundingService.getReceivedDonations(userSeq);
            } else {
                return ResponseEntity.badRequest()
                        .body(new SimpleResponseDTO(false, "유효하지 않은 후원 유형입니다.", null));
            }

            return ResponseEntity.ok(donations);

        } catch (IllegalStateException e) {
            logger.error("Invalid Principal Type: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new SimpleResponseDTO(false, "유효하지 않은 인증 정보입니다.", null));
        } catch (Exception e) {
            logger.error("Error occurred while fetching donations: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new SimpleResponseDTO(false, "후원 내역 조회 중 오류가 발생했습니다.", null));
        }
    }
}