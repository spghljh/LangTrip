package kr.co.sist.e_learning.quiz;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QuizMapper {
	
	//<insert id="insertQuizList" parameterType="kr.co.sist.elearning.quiz.QuizListDTO">
	public void insertQuizList(QuizListDTO quizList);

	//<insert id="insertQuiz" parameterType="quizDTO">
	public void insertQuiz(QuizDTO qDTO);
	
	//<insert id="insertQuizOption" parameterType="kr.co.sist.elearning.quiz.QuizOptionDTO">
	public void insertQuizOption(QuizOptionDTO optionDTO);

	//<select id="selectQuizWithOptions" parameterType="String" resultMap="quizWithOptionsMap">
	List<QuizDTO> selectQuizWithOptions(String quizListSeq);

	//<select id="selectQuizListInfo" parameterType="String" resultType="kr.co.sist.elearning.quiz.QuizListDTO">
	public QuizListDTO selectQuizListInfo(String quizListSeq);
	
	//<select id="selectQuiz" parameterType="String" resultType="kr.co.sist.elearning.quiz.QuizDTO">
	public QuizDTO selectQuiz(String quizSeq);
	
	//<insert id="insertQuizResponse" parameterType="kr.co.sist.elearning.quiz.QuizResponseDTO">
	public void insertQuizResponse(QuizResponseDTO qrDTO);
	
	//<select id="existsQuizResponse" resultType="boolean">
	public boolean existsQuizResponse(@Param("quizSeq") String quizSeq, @Param("userSeq") String userSeq);

	//<select id="QuizCorrectChk" resultType="kr.co.sist.elearning.quiz.QuizStatusDTO">
	public QuizStatusDTO QuizCorrectChk(QuizStatusDTO qsDTO);
	
	//<select id="selectQuizListTable" parameterType="kr.co.sist.elearning.quiz.QuizListDTO">
	public List<QuizListDTO> selectQuizListTable();
	
	//<select id="selectCorrectContent" resultType="String">
	String selectCorrectContent(String quizSeq);
	
	//<update id="updateQuizList">
	public void updateQuizList(@Param("title") String title, @Param("langCategory") String langCategory,@Param("quizListSeq") String quizListSeq);
	
	//<update id="updateQuiz" parameterType="kr.co.sist.elearning.quiz.QuizDTO">
	public void updateQuiz(QuizDTO quiz);
	
	//<delete id="deleteQuizByListSeq">
	public void deleteQuizByListSeq(@Param("quizListSeq") String quizListSeq);

	//<delete id="deleteOptionsByQuizSeq" parameterType="String">
	public void deleteOptionsByQuizSeq(@Param("quizSeq") String quizSeq);
	
	//<update id="softDeleteQuizList">
	public void softDeleteQuizList(@Param("quizListSeq") String quizListSeq);
	
	//<update id="softDeleteQuiz">
	public void softDeleteQuiz(@Param("quizListSeq") String quizListSeq);

	//<update id="softDeleteOptions" parameterType="String">
	public void softDeleteOptions(@Param("quizListSeq") String quizListSeq);

	public List<QuizListDTO> selectQuizByCourseSeq(String courseSeq);
	
	public List<QuizListDTO> selectQuizSeqByCourseId(String courseSeq);
	
	public List<QuizListDTO> selectDistinctQuizLists(String courseSeq);
}
