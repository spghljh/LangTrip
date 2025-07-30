package kr.co.sist.e_learning.quiz;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuizResponseDTO {
	
	 private String quizResponseSeq; 
	 private String quizListSeq; 
	 private String quizSeq; 
	 private String courseSeq;  
	 private String userSeq;  
	 private int selectOption; //유저가 선택한 보기
	 private String correctCheck; //정답(Y) or 오답(N) 체크
	 private Date responseDate;	
	 
	
}
