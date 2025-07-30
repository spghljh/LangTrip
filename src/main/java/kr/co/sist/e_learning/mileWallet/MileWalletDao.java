package kr.co.sist.e_learning.mileWallet;

import org.apache.ibatis.annotations.Param;

public interface MileWalletDao {

    // 1. 유저 번호로 지갑 SEQ 조회
    Long selectWalletSeqByUserSeq(Long userSeq);

    // 2. 새 지갑 생성
    Long insertMileWallet(MileWalletDTO mileWalletDTO);

    // 3. 총 마일 잔고(TOTAL_MILES) 변경 (충전 시 사용)
    void updateMileBalance(Long walletSeq, Long addMileAmount);

    // 4. 후원 가능 마일 차감/증가 (DONATION_AVAILABLE 조정)
    void updateDonationAvailable(@Param("walletSeq") Long walletSeq, @Param("amount") Long amount);

    // 5. 현재 마일 조회 (TOTAL_MILES)
    Long selectCurrentMiles(@Param("walletSeq") Long walletSeq);

    // 6. 후원 누적 마일 증가
    void addDonatedMiles(@Param("walletSeq") Long walletSeq, @Param("amount") Long amount);

    // 7. 받은 누적 마일 증가
    void addReceivedMiles(@Param("walletSeq") Long walletSeq, @Param("amount") Long amount);

    // 8. 지갑 존재 여부 확인
    boolean checkWalletExists(Long walletSeq);
}
