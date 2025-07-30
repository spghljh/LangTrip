package kr.co.sist.e_learning.donation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureDetailDTO {
    private String courseSeq;
    private String courseTitle;
    private Long instructorUserSeq;
    private String instructorNickname;
    private String instructorWalletSeq;
}
