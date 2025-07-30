package kr.co.sist.e_learning.user.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
    
    boolean existsByUserId(String userId);

    UserEntity findByUserId(String userId);
    
    UserEntity findByNickname(String nickname);
    
    UserEntity findByEmail(String email);
    
    Optional<UserEntity> findByUserSeq(Long userSeq);

    Optional<UserEntity> findBySocialProviderAndSocialId(String socialProvider, String socialId);
    
    
}
