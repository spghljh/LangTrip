package kr.co.sist.e_learning.mypage;

import java.util.List;

public interface FundingService {
    FundingDTO getAccountInfo(Long userSeq);
    List<FundingDTO> getUserFundings(Long userSeq);
    List<FundingDTO> getReceivedDonations(Long userSeq);
    List<FundingDTO> getPaymentHistory(Long userSeq);
}
