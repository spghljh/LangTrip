package kr.co.sist.e_learning.admin.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.sist.e_learning.common.aop.Loggable;

@Service
public class AdminAuthServiceImpl implements AdminAuthService {

    @Autowired
    private AdminAuthDAO adminAuthDAO;

	
    @Override
    @Loggable(actionType = "ADMIN_LOGIN")
    public AdminAuthDTO login(String id, String pw) {


        AdminAuthDTO admin = adminAuthDAO.loginSelectAdminById(id);

        if (admin == null || !"Y".equals(admin.getStatus())) {
            return null;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (encoder.matches(pw, admin.getAdminPw())) {
            return admin; // ✅ 로그인 성공
        }

        return null; // ❌ 비밀번호 틀림
    }
}
