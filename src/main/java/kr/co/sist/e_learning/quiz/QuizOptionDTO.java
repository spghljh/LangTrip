package kr.co.sist.e_learning.quiz;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuizOptionDTO {
	 private String quizOptionSeq; //pk
	 private String quizSeq; //fk 퀴즈
	 private int optionOrder;// 보기 번호 (1~5지선다)
	 private String content; // 보기 내용
	 private String imageName;//저장된 이미지 파일명
	 private String imageAddr;//이미지 경로
	 private Integer fileIndex; // 이미지가 있는 경우만 세팅 + 이미지 위치 추적
	 private String isDelete;
}
