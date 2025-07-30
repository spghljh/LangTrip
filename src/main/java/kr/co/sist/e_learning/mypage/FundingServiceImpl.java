package kr.co.sist.e_learning.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundingServiceImpl implements FundingService {

    private final FundingDAO fundingDAO;

    @Autowired
    public FundingServiceImpl(FundingDAO fundingDAO) {
        this.fundingDAO = fundingDAO;
    }

    @Override
    public FundingDTO getAccountInfo(Long userSeq) {
        return fundingDAO.selectAccountInfo(userSeq);
    }

    @Override
    public List<FundingDTO> getUserFundings(Long userSeq) {
        return fundingDAO.selectMyDonations(userSeq);
    }

    @Override
    public List<FundingDTO> getReceivedDonations(Long userSeq) {
        return fundingDAO.selectReceivedDonations(userSeq);
    }

    @Override
    public List<FundingDTO> getPaymentHistory(Long userSeq) {
        return fundingDAO.selectMyPayments(userSeq);
    }
}
