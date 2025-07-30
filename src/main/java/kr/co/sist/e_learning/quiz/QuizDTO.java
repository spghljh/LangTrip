package kr.co.sist.e_learning.quiz;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuizDTO {
  
	private String quizSeq; //pk
	private String quizListSeq; //fk quiz_list
	private String courseSeq; //fk 강의랑 연결
    private String type; //퀴즈 유형 (text-quiz or img-quiz)
    private String question; //문제 
    private Integer correctOption; //정답인 보기 번호
    private int quizOrder; //문제 순서
    private String isDelete; //soft delete용 0 or 1로 처리
    private List<QuizOptionDTO> options= new ArrayList<>(); //보기 묶음
    private Integer fileIndex; // 이미지가 있을 때 index
}