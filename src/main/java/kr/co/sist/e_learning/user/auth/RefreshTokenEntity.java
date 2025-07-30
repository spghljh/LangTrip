package kr.co.sist.e_learning.user.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "REFRESH_TOKEN")
@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq_gen")
    @SequenceGenerator(
            name = "refresh_token_seq_gen",
            sequenceName = "REFRESH_TOKEN_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID", nullable = false, length = 36)
    private String userId;

    @Column(name = "REFRESH_TOKEN", nullable = false, length = 512, unique = true)
    private String refreshToken;

    @Column(name = "EXPIRES_AT", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    public RefreshTokenEntity(String userId, String refreshToken, LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }
}
