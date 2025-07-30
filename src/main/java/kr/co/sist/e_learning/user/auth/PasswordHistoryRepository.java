package kr.co.sist.e_learning.user.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistoryEntity, Long> {

    List<PasswordHistoryEntity> findByUser_UserSeq(Long userSeq);
}
