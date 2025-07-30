package kr.co.sist.e_learning.payment;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDaoImpl implements PaymentDao {

    private static final String NAMESPACE = "kr.co.sist.e_learning.payment.PaymentMapper";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public String selectNextPaymentSeq() {
        return sqlSession.selectOne(NAMESPACE + ".selectNextPaymentSeq");
    }

    @Override
    public void insertPayment(PaymentRequestDTO paymentRequestDTO) {
        // null 방지: 문자열 컬럼들
        if (paymentRequestDTO.getPaymentStatus() == null) {
            paymentRequestDTO.setPaymentStatus("SUCCESS");
        }
        if (paymentRequestDTO.getMethod() == null) {
            paymentRequestDTO.setMethod("");
        }
        if (paymentRequestDTO.getProvider() == null) {
            paymentRequestDTO.setProvider("");
        }
        if (paymentRequestDTO.getCurrency() == null) {
            paymentRequestDTO.setCurrency("KRW");
        }
        if (paymentRequestDTO.getChannel() == null) {
            paymentRequestDTO.setChannel("WEB");
        }
        if (paymentRequestDTO.getName() == null) {
            paymentRequestDTO.setName("");
        }
        if (paymentRequestDTO.getReceiptUrl() == null) {
            paymentRequestDTO.setReceiptUrl("");
        }
        if (paymentRequestDTO.getMileAmount() == null) {
            paymentRequestDTO.setMileAmount(0L);
        }
        if (paymentRequestDTO.getFeeRate() == null) {
            paymentRequestDTO.setFeeRate(10.0);
        }

        sqlSession.insert(NAMESPACE + ".insertPayment", paymentRequestDTO);
    }
}
