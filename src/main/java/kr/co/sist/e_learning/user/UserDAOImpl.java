package kr.co.sist.e_learning.user;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SqlSession session;

    @Override
    public UserDTO selectUserByEmailAndPassword(String email, String password) {
        Map<String, String> param = new HashMap<>();
        param.put("email", email);
        param.put("password", password);
        return session.selectOne("kr.co.sist.e_learning.user.UserMapper.selectUserByEmailAndPassword", param);
    }
}
