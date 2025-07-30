package kr.co.sist.e_learning.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDTO login(String email, String password) {
        return userDAO.selectUserByEmailAndPassword(email, password);
    }
}
