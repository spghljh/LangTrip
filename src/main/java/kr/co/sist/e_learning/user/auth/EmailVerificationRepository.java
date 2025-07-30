package kr.co.sist.e_learning.user.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository
extends JpaRepository<EmailVerificationEntity, String> {

EmailVerificationEntity findByVerificationSeq(String verificationSeq);
}

