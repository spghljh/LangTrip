package kr.co.sist.e_learning.user.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "PASSWORD_HISTORY")
@Getter
@Setter
public class PasswordHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_history_seq_gen")
    @SequenceGenerator(
            name = "password_history_seq_gen",
            sequenceName = "PASSWORD_HISTORY_SEQ",
            allocationSize = 1
    )
    @Column(name = "HISTORY_ID")
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SEQ", nullable = false)
    private UserEntity user;

    @Column(name = "PASSWORD_HASH", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
}

