package kr.co.sist.e_learning.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureHistoryDTO {
	private String courseSeq; 
    private String courseTitle;
    private String videoTitle;
    private String instructorName;
    private String uploadDate;
    private double courseRate;
}
