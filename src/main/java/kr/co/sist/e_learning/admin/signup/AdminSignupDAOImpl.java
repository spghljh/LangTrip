package kr.co.sist.e_learning.admin.signup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class AdminSignupDAOImpl implements AdminSignupDAO {

    @Autowired
    private SqlSession sqlSession;

    private static final String NS = "kr.co.sist.e_learning.admin.signup.AdminSignupDAO.";

    @Override
    public void insertVerificationCode(EmailVerificationDTO dto) {
        sqlSession.insert(NS + "insertVerificationCode", dto);
    }

    @Override
    public EmailVerificationDTO selectValidVerification(String email, String code) {
        Map<String, String> param = new HashMap<>();
        param.put("email", email);
        param.put("code", code);
        return sqlSession.selectOne(NS + "selectValidVerification", param);
    }

    @Override
    public void markCodeVerified(String verificationSeq) {
        sqlSession.update(NS + "markCodeVerified", verificationSeq);
    }

    @Override
    public void insertSignupRequest(AdminSignupDTO dto) {
        sqlSession.insert(NS + "insertSignupRequest", dto);
    }


    @Override
    public void insertSignupPermission(Map<String, String> param) {
        sqlSession.insert(NS + "insertSignupPermission", param);
    }


    @Override
    public boolean isDuplicateId(String adminId) {
        int count = sqlSession.selectOne("kr.co.sist.e_learning.admin.signup.AdminSignupDAO.isDuplicateId", adminId);
        return count > 0;
    }
    @Override
    public List<String> selectRoleCodesByDept(String dept) {
        return sqlSession.selectList(NS + "selectRoleCodesByDept", dept);
    }

}
