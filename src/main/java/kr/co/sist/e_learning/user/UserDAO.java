package kr.co.sist.e_learning.user;

public interface UserDAO {
    UserDTO selectUserByEmailAndPassword(String email, String password);
}
