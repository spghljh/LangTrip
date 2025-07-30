package kr.co.sist.e_learning.payment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;


@Controller
public class PaymentController {

	@GetMapping("user/payments")
	public String getMethodName() {
		return "user/payments/payments";
	}

	@GetMapping("/payment/success")
	public String paymentSuccessPage(
			@RequestParam("lectureNo") String lectureNo,
			Model model,
			HttpSession session) {

		// 세션에서 결제 금액과 마일 정보 가져오기
		Long paymentAmount = (Long) session.getAttribute("paymentAmount");
		Long mileAmount = (Long) session.getAttribute("mileAmount");
		String storedLectureNo = (String) session.getAttribute("lectureNoForSuccessPage");

		// 모델에 추가
		model.addAttribute("paymentAmount", paymentAmount);
		model.addAttribute("mileAmount", mileAmount);
		model.addAttribute("lectureNo", storedLectureNo != null ? storedLectureNo : lectureNo);

		// 세션에서 속성 제거 (한 번 사용 후 삭제)
		session.removeAttribute("paymentAmount");
		session.removeAttribute("mileAmount");
		session.removeAttribute("lectureNoForSuccessPage");

		return "user/payments/payment_success";
	}
}

