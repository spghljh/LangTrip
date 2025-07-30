package kr.co.sist.e_learning.payment;

public interface PaymentService {
	 /**
     * 결제 등록
     */
    void savePayment(PaymentRequestDTO dto);

    /**
     * 결제 취소 처리
     */
    void cancelPayment(PaymentRequestDTO dto);
}