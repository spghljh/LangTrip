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
public class QuizListDTO {
	
	 private String quizListSeq; //pk
	 private String courseSeq;   //fk
	 private String langCategory;//언어 분류 (영어,중국어,일본어)
	 private String title;       //퀴즈묶음제목
	 private Date uploadDate;	//생성일
	 private String status;       //상태 -> 학습전/학습중(중도포기)/학습완료
	 private String isDelete;       //상태 -> 학습전/학습중(중도포기)/학습완료
	 //퀴즈묶음
	 private List<QuizDTO> quiz = new ArrayList<>();
	 
//	 //switch case로 langCategory 값 한국어로 매핑
//	 public String getLangCategoryKorean() {
//		 	String language = null;
//		    switch (langCategory) {
//			    case "en":
//			    	language = "영어";
//			    	break;
//		        case "jp":
//		        	language = "일본어";
//		            break;
//		        case "ch":
//		        	language = "중국어";
//		            break;
//		    }
//		 return language;
//	 }//langCategoryKorean
}
