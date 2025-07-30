package kr.co.sist.e_learning.payment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.sist.e_learning.mileWallet.MileWalletDTO;
import kr.co.sist.e_learning.mileWallet.MileWalletDao;


@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private MileWalletDao mileWalletDao;

    @Override
    @Transactional
    public void savePayment(PaymentRequestDTO paymentDTO) {
        logger.info("[PAYMENT-DEBUG] ----- savePayment 시작 -----");
        logger.info("[PAYMENT-DEBUG] 요청 DTO: {}", paymentDTO);

        // 1. 지갑 존재 확인
        logger.info("[PAYMENT-DEBUG] 1. 지갑 존재 확인 시작, userSeq: {}", paymentDTO.getUserSeq());
        Long walletSeq = mileWalletDao.selectWalletSeqByUserSeq(paymentDTO.getUserSeq());
        if (walletSeq == null) {
            logger.info("[PAYMENT-DEBUG] 지갑 없음. 새로 생성합니다.");
            // 지갑 없으면 새로 생성
            MileWalletDTO walletDTO = new MileWalletDTO();
            walletDTO.setUserSeq(paymentDTO.getUserSeq());
            walletDTO.setTotalMiles(0L);
            walletDTO.setDonation_available(0L);
            walletDTO.setDonated_miles(0L);
            walletDTO.setReceived_miles(0L);

            walletSeq = mileWalletDao.insertMileWallet(walletDTO);
            logger.info("[PAYMENT-DEBUG] 지갑 생성 완료. walletSeq: {}", walletSeq);
        }
        paymentDTO.setWalletSeq(walletSeq);
        logger.info("[PAYMENT-DEBUG] 1. 지갑 확인 완료. walletSeq: {}", walletSeq);

        // 2. 명시적으로 시퀀스 채번 및 DTO에 설정
        logger.info("[PAYMENT-DEBUG] 2. payments 시퀀스 채번 시작");
        String nextPaymentSeq = paymentDao.selectNextPaymentSeq();
        paymentDTO.setMerchantUid(nextPaymentSeq);
        logger.info("[PAYMENT-DEBUG] 2. 채번된 시퀀스 (merchantUid): {}", nextPaymentSeq);

        // 3. payments 테이블 insert
        logger.info("[PAYMENT-DEBUG] 3. payments 테이블 INSERT 시작");
        paymentDao.insertPayment(paymentDTO);
        logger.info("[PAYMENT-DEBUG] 3. payments 테이블 INSERT 성공");

        // 5. mile_wallet 충전
        logger.info("[PAYMENT-DEBUG] 5. 마일리지 충전 시작. walletSeq: {}, amount: {}", walletSeq, paymentDTO.getMileAmount());
        mileWalletDao.updateMileBalance(walletSeq, paymentDTO.getMileAmount());
        logger.info("[PAYMENT-DEBUG] 5. 마일리지 충전 성공");
        logger.info("[PAYMENT-DEBUG] ----- savePayment 정상 종료. 트랜잭션 커밋 예정 -----");
    }


    @Override
    @Transactional
    public void cancelPayment(PaymentRequestDTO paymentDTO) {
        logger.info("[PAYMENT-DEBUG] ----- cancelPayment 시작 -----");
        logger.info("[PAYMENT-DEBUG] 요청 DTO: {}", paymentDTO);

        // 1. 지갑 존재 확인 (취소된 결제도 지갑과 연결되어야 함)
        logger.info("[PAYMENT-DEBUG] 1. 지갑 존재 확인 시작, userSeq: {}", paymentDTO.getUserSeq());
        Long walletSeq = mileWalletDao.selectWalletSeqByUserSeq(paymentDTO.getUserSeq());
        if (walletSeq == null) {
            MileWalletDTO walletDTO = new MileWalletDTO();
            walletDTO.setUserSeq(paymentDTO.getUserSeq());
            walletDTO.setTotalMiles(0L);
            walletDTO.setDonation_available(0L);
            walletDTO.setDonated_miles(0L);
            walletDTO.setReceived_miles(0L);
            walletSeq = mileWalletDao.insertMileWallet(walletDTO);
            logger.info("[PAYMENT-DEBUG] 지갑 생성 완료. walletSeq: {}", walletSeq);
        }
        // payments 테이블에 "CANCELLED" 상태로 insert
        paymentDTO.setPaymentStatus("CANCELLED");

        if (paymentDTO.getProvider() == null) {
            paymentDTO.setProvider("CANCELLED");
        }

        if (paymentDTO.getMethod() == null) {
            paymentDTO.setMethod("CANCELLED");
        }

        // 2. 명시적으로 시퀀스 채번 및 DTO에 설정
        logger.info("[PAYMENT-DEBUG] 2. payments 시퀀스 채번 시작");
        String nextPaymentSeq = paymentDao.selectNextPaymentSeq();
        paymentDTO.setMerchantUid(nextPaymentSeq);
        paymentDTO.setWalletSeq(walletSeq);
        logger.info("[PAYMENT-DEBUG] 2. 채번된 시퀀스 (merchantUid): {}", nextPaymentSeq);

        // 3. payments 테이블 insert
        logger.info("[PAYMENT-DEBUG] 3. payments 테이블 CANCELLED 상태로 INSERT 시작");
        paymentDao.insertPayment(paymentDTO);
        logger.info("[PAYMENT-DEBUG] 3. payments 테이블 CANCELLED 상태로 INSERT 성공");
        logger.info("[PAYMENT-DEBUG] ----- cancelPayment 정상 종료. 트랜잭션 커밋 예정 -----");
    }
}


