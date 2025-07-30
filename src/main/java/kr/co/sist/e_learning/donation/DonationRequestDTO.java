package kr.co.sist.e_learning.donation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DonationRequestDTO {
    private String lectureNo;
    private Long instructorUserSeq; // instructor 지갑은 서버에서 찾음
    private Long amount;
    private String message;
}

