package kr.co.sist.e_learning.quiz;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface QuizService {
	
	//등록
	public void addQuiz(QuizListDTO quizListDTO, List<MultipartFile> imageFiles) throws Exception;
	
	//퀴즈 시작 전 강의실 화면용 (퀴즈 제목/외국어 종류 보여주기)
	public QuizListDTO getQuizListInfo(String quizListSeq);
	
	//퀴즈 가져오기
	public Map<String, Object> getQuizList(String quizListSeq, String userSeq);
	
	//퀴즈 응답 저장
	public void saveQuizResponse(QuizResponseDTO qrDTO);
	
	//강의실 퀴즈 리스트 info
	public List<QuizListDTO> getAllQuizList(String userSeq);
	
	//퀴즈 학습완료 화면
	public Map<String, Object> getQuizCompletionStats(String userSeq, String quizListSeq);
	
	//퀴즈 수정용 : 등록된 데이터 불러오기
	public QuizListDTO getQuizListForModify(String quizListSeq);
	 
	 //퀴즈 수정 처리
	 public void updateQuiz(String quizListSeq, String quizJson, List<MultipartFile> imageFiles) throws Exception;
	
	 //퀴즈 soft delete
	 public void softDeleteAllQuiz(String quizListSeq);
	 
	//퀴즈 정답 알려주기
	//Map<String, Object> processSubmitAnswer(QuizResponseDTO qrDTO);
	
	 public List<QuizListDTO> searchQuizByCourseSeq(String courseSeq);
	 
	 public List<QuizListDTO> searchQuizSeqByCoursSEq(String courseSeq);
	 
	 public List<QuizListDTO> searchDistinctQuizLists(String courseSeq);

}
