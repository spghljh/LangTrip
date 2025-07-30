package kr.co.sist.e_learning.admin.auth;

public interface AdminAuthService {
    AdminAuthDTO login(String id, String pw);
}
