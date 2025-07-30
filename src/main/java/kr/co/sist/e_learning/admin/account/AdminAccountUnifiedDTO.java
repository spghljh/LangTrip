package kr.co.sist.e_learning.admin.account;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AdminAccountUnifiedDTO {
    // ✅ 공통 필드 (관리자 & 가입신청자)
    private String adminId;         // ID 또는 이메일
    private String adminName;
    private String email;
    private String phone;
    private String status;          // ACTIVE / INACTIVE / PENDING / REJECTED
    private LocalDateTime createdAt;
    private List<String> roleCodes; // ROLE_SUPER 등

    // ✅ 가입신청자 전용 필드
    private String requestId;       // 가입신청 고유 키
    private String dept;            // 부서명
    private String inline;            // 부서명
    private String passwordHash;    // 가입 승인 시 비밀번호 필요 시
}
