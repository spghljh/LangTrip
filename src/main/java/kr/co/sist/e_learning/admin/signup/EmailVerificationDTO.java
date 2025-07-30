package kr.co.sist.e_learning.admin.signup;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class EmailVerificationDTO {
    private String verificationSeq;
    private String adminId;
    private String email;
    private String code;
    private String status;
    private Timestamp createdAt;
    private Timestamp expiresAt;
}

