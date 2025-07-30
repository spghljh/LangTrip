package kr.co.sist.e_learning.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MyPageDAOImpl implements MyPageDAO {
    @Autowired
    private SqlSession session;

    @Autowired
    private SqlSessionTemplate sst;
    
    private final String nsMP = "kr.co.sist.e_learning.mypage.MyPageMapper";

    @Override
    public MyPageDTO getUserInfo(long userSeq) {
    	return session.selectOne(nsMP + ".selectUserInfo", userSeq);
    }
    
    @Override
    public String selectProfilePath(long userSeq) {
        return session.selectOne(nsMP + ".selectProfilePath", userSeq);
    }
    
    @Override
    public void updateProfile(long userSeq, String profile) {
        Map<String, Object> map = new HashMap<>();
        map.put("userSeq", userSeq);
        map.put("profile", profile);
        session.update(nsMP + ".updateProfile", map);
    }

  
    @Override
    public List<SubscriptionDTO> selectSubscriptions(Long userSeq) {
        System.out.println("[DAO] selectSubscriptions(userSeq=" + userSeq + ")");
        List<SubscriptionDTO> list = session.selectList(nsMP + ".selectSubscriptions", userSeq);
        System.out.println("[DAO] session.selectList → size=" + list.size() + ", list=" + list);
        return list;
    }

    @Override
    public int deleteSubscription(Map<String, Object> paramMap) {
        System.out.println("[DAO] deleteSubscription(paramMap=" + paramMap + ")");
        int count = session.delete(nsMP + ".deleteSubscription", paramMap);
        System.out.println("[DAO] session.delete → count=" + count);
        return count;
    }
    
    
}