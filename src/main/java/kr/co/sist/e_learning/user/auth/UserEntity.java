package kr.co.sist.e_learning.user.auth;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@SequenceGenerator(
	    name = "user_seq_generator",       // JPA 시퀀스 이름
	    sequenceName = "USER_SEQ",         // 실제 Oracle SEQUENCE 이름
	    allocationSize = 1                 // 시퀀스 증가 단위
	)
public class UserEntity {



	 @Id
    @SequenceGenerator(
        name = "user_seq_generator",
        sequenceName = "USER_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_seq_generator"
    )
    @Column(name = "USER_SEQ")
    private Long userSeq;

	  


    @Column(name = "USER_ID", nullable = true, unique = true, length = 20)
    private String userId;

    @Column(name = "EMAIL", nullable = true,unique = true, length = 50)
    private String email;

    @Column(name = "NICKNAME", nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(name = "PASSWORD", length = 100)
    private String password;

    @Column(name = "SOCIAL_PROVIDER", nullable = true, length = 20)
    private String socialProvider;

    @Column(name = "SOCIAL_ID")
    private String socialId;

    @Column(name = "SIGNUP_PATH", nullable = false, length = 50)
    private String signupPath;

    @Column(name = "PROFILE", nullable = true, length = 255)
    private String profile;

    @Column(name = "STATUS", nullable = true, length = 20)
    private String status;

    @Column(name = "PASSWORD_STATUS", nullable = true, length = 20)
    private String passwordStatus;

    @Column(name = "CREATED_AT", nullable = true)
    private LocalDateTime createdAt;
    
    
}
