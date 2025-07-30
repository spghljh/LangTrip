package kr.co.sist.e_learning.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 퀴즈 정답률 계산, 상태 (학습전/학습중/학습완료) 계산용 DTO
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class QuizStatusDTO {
  
	private int totalCnt; //총 응답 수
	private int correctCnt; //정답 수
	private String userSeq;
	private String quizListSeq;
	
	public QuizStatusDTO(String userSeq, String quizListSeq) {
		this.userSeq = userSeq;
		this.quizListSeq = quizListSeq;
	}
}