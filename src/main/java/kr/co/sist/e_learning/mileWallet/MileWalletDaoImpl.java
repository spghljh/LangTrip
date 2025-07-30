package kr.co.sist.e_learning.mileWallet;


import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MileWalletDaoImpl implements MileWalletDao {

    private static final String NAMESPACE = "kr.co.sist.e_learning.MileWalletMapper";

    @Autowired
    private SqlSession sqlSession;

    @Override
    public Long selectWalletSeqByUserSeq(Long userSeq) {
        return sqlSession.selectOne(NAMESPACE + ".selectWalletSeqByUserSeq", userSeq);
    }

    @Override
    public Long insertMileWallet(MileWalletDTO mileWalletDTO) {
        sqlSession.insert(NAMESPACE + ".insertMileWallet", mileWalletDTO);
        return mileWalletDTO.getWalletSeq();
    }

    @Override
    public void updateMileBalance(Long walletSeq, Long addMileAmount) {
        Map<String, Object> params = new HashMap<>();
        params.put("walletSeq", walletSeq);
        params.put("addMileAmount", addMileAmount);
        sqlSession.update(NAMESPACE + ".updateMileBalance", params);
    }

    @Override
    public Long selectCurrentMiles(Long walletSeq) {
        return sqlSession.selectOne(NAMESPACE + ".selectCurrentMiles", walletSeq);
    }

    @Override
    public void updateDonationAvailable(Long walletSeq, Long amount) {
        Map<String, Object> params = new HashMap<>();
        params.put("walletSeq", walletSeq);
        params.put("amount", amount);
        sqlSession.update(NAMESPACE + ".updateDonationAvailable", params);
    }

    @Override
    public void addDonatedMiles(Long walletSeq, Long amount) {
        Map<String, Object> params = new HashMap<>();
        params.put("walletSeq", walletSeq);
        params.put("amount", amount);
        sqlSession.update(NAMESPACE + ".addDonatedMiles", params);
    }

    @Override
    public void addReceivedMiles(Long walletSeq, Long amount) {
        Map<String, Object> params = new HashMap<>();
        params.put("walletSeq", walletSeq);
        params.put("amount", amount);
        sqlSession.update(NAMESPACE + ".addReceivedMiles", params);
    }

    @Override
    public boolean checkWalletExists(Long walletSeq) {
        return sqlSession.selectOne(NAMESPACE + ".checkWalletExists", walletSeq);
    }
}
