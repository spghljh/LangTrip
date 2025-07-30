package kr.co.sist.e_learning.user.auth;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "email_verification")
@Getter
@Setter
public class EmailVerificationEntity {

    @Id
    private String verificationSeq; // UUID

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 10)
    private String code;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private Timestamp expiresAt;
}
