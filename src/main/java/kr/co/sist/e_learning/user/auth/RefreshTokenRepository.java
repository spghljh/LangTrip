package kr.co.sist.e_learning.user.auth;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    // refresh token 문자열로 검색
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    // 특정 userId의 토큰 전체 삭제 (로그아웃 시 주로 사용)
    void deleteByUserId(String userId);

    // 특정 refresh token 문자열로 삭제
    void deleteByRefreshToken(String refreshToken);

    // 만료된 토큰 일괄 삭제
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity t WHERE t.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
}
