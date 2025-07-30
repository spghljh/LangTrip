package kr.co.sist.e_learning.payment;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
	void insertPayment(PaymentRequestDTO dto);

}
