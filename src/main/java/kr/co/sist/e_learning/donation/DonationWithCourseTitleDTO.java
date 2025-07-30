package kr.co.sist.e_learning.donation;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DonationWithCourseTitleDTO {

	 private String donationSeq;     // 후원 시퀀스
	    private String courseSeq;       // 강의 시퀀스
	    private Long sponsorWalletSeq;  // 후원자 지갑 시퀀스
	    private Long instructorWalletSeq; // 강사 지갑 시퀀스
	    private Long amount;            // 후원 마일
	    private String message;         // 메시지
	    private LocalDateTime createdAt; // 생성일
	    private String courseTitle;     // 강의 제목 (추가된 부분)
}
