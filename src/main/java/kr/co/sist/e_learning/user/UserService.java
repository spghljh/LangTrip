package kr.co.sist.e_learning.user;

public interface UserService {
    UserDTO login(String email, String password);
}
