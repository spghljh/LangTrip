package kr.co.sist.e_learning.payment;

public interface PaymentDao {

    String selectNextPaymentSeq();

    void insertPayment(PaymentRequestDTO paymentRequestDTO);
   
    
    
}
