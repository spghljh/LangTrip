package kr.co.sist.e_learning.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

/**
 * 결제 관련 컨트롤러
 */
@RestController
@RequestMapping("/payment")
public class PaymentRestController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 결제 처리
     */
    @PostMapping
    public ResponseEntity<?> savePayment(@RequestBody PaymentRequestDTO paymentRequestDTO, HttpSession session) {
        try {
            // 현재 로그인된 사용자 정보 가져오기
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userSeq;

            // 사용자 정보가 Long 타입인지 확인
            if (principal instanceof Long) {
                userSeq = (Long) principal;
            } else {
                throw new IllegalStateException("User principal is not of expected type Long");
            }

            // PaymentRequestDTO에 userSeq 설정
            paymentRequestDTO.setUserSeq(userSeq);

            // 결제 처리
            paymentService.savePayment(paymentRequestDTO);

            // 결제 금액과 마일 정보를 세션에 저장
            session.setAttribute("paymentAmount", paymentRequestDTO.getPaymentAmount());
            session.setAttribute("mileAmount", paymentRequestDTO.getMileAmount());
         

            // 결제 성공 응답 반환
            // redirectUrl은 lectureNo만 URL 쿼리 파라미터로 넘기고, POST 방식으로 결제 정보를 처리
            String redirectUrl = "/payment/success"; // GET 방식으로 lectureNo만 넘기기

            // 결제 성공 응답 반환
            return ResponseEntity.ok().body(new PaymentSuccessResponseDTO(
                    true, "결제가 정상 처리되었습니다.", redirectUrl,
                    paymentRequestDTO.getPaymentAmount(), paymentRequestDTO.getMileAmount()
            ));

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body(new PaymentSimpleResponseDTO(false, "결제 처리 중 오류가 발생했습니다."));
        }
    }



    /**
     * 결제 중간 취소 (사용자 결제 창에서 취소 눌렀을 때 등)
     */
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userSeq;
            if (principal instanceof Long) {
                userSeq = (Long) principal;
            } else {
                throw new IllegalStateException("User principal is not of expected type Long");
            }
            paymentRequestDTO.setUserSeq(userSeq);
            paymentService.cancelPayment(paymentRequestDTO);
            return ResponseEntity.ok().body(new PaymentSimpleResponseDTO(true, "결제가 취소되었습니다."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .internalServerError()
                    .body(new PaymentSimpleResponseDTO(false, "결제 취소 중 오류가 발생했습니다."));
        }
    }
}

