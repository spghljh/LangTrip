package kr.co.sist.e_learning.admin.signup;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSignupDTO {
    private String requestId;
    private String adminId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String dept;
    private String inline; // ✅ 추가됨
    private String emailCode;
    // 권한은 자동 매핑이므로 삭제 또는 사용하지 않음
}

