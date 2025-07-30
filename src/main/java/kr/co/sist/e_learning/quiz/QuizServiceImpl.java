package kr.co.sist.e_learning.quiz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizMapper quizMapper;

    @Value("${upload.saveDirQuiz}")
    private String saveDir;
    
    // 강의실 전체 퀴즈 목록
    @Override
    public List<QuizListDTO> getAllQuizList(String userSeq) {
    	
    	List<QuizListDTO> quizList = quizMapper.selectQuizListTable();

        for (QuizListDTO q : quizList) {
            int total = quizMapper.selectQuizWithOptions(q.getQuizListSeq()).size();

            QuizStatusDTO param = new QuizStatusDTO();
            param.setQuizListSeq(q.getQuizListSeq());
            param.setUserSeq(userSeq);

            QuizStatusDTO statusDTO = quizMapper.QuizCorrectChk(param);
            int response = statusDTO.getTotalCnt();

            if (response == 0) {
                q.setStatus("학습전");
            } else if (response < total) {
                q.setStatus("학습중");
            } else if (response == total) {
                q.setStatus("학습완료");
            } else {
                throw new IllegalStateException("응답 수 > 총 퀴즈 수: " + response + " > " + total);
            }
            
        }

        return quizList;
    }
    
    //단일 퀴즈의 상세 정보 : 시작 전 모달창 정보 (퀴즈 제목/외국어 종류/상태)
    @Override
    public QuizListDTO getQuizListInfo(String quizListSeq) {
       
    	//퀴즈 기본 정보(제목,언어,상태) 조회
    	QuizListDTO quizListDTO = quizMapper.selectQuizListInfo(quizListSeq);
        
        String userSeq = "user001";

        List<QuizDTO> quizList = quizMapper.selectQuizWithOptions(quizListSeq);
      
        // 응답 수 조회
        QuizStatusDTO quizStatus = new QuizStatusDTO();
        quizStatus.setQuizListSeq(quizListSeq);
        quizStatus.setUserSeq(userSeq);

        QuizStatusDTO qsDTO = quizMapper.QuizCorrectChk(quizStatus);
        int totalQuizCnt = quizList.size(); //총 퀴즈 수
        int responseCnt = qsDTO.getTotalCnt();   //사용자의 응답 수

        System.out.println("✅ 총 퀴즈 수: " + totalQuizCnt);
        System.out.println("✅ 응답 수: " + responseCnt);
        
        // 상태 판단 (학습전/학습중(중도포기)/학습완료)
        String status;
        if (responseCnt == 0) {
            status = "학습전";
        } else if (responseCnt < totalQuizCnt) {
            status = "학습중";
        } else if (responseCnt == totalQuizCnt) {
            status = "학습완료";
        } else {
        	//예외 상황: 중복 저장같은 버그로 응답 수가 총 퀴즈 수보다 더 많아질 경우
         throw new IllegalStateException("응답 수: "+responseCnt + " > 총 퀴즈 수: " + totalQuizCnt);
        }

        quizListDTO.setStatus(status);
        
        return quizListDTO;
    }//getQuizListInfo
    
    //퀴즈 등록
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addQuiz(QuizListDTO quizListDTO, List<MultipartFile> imageFiles) throws Exception {
    	
    	  // ✅ 필수값 누락 방지 (NOT NULL 임시값)
        if (quizListDTO.getCourseSeq() == null || quizListDTO.getCourseSeq().isBlank()) {
            quizListDTO.setCourseSeq("COURSE001"); // 또는 "0" 등 규칙에 맞게 설정
        }
    	
        // 1. 퀴즈 묶음 등록
        quizMapper.insertQuizList(quizListDTO);

        int quizOrder = 1;
        for (QuizDTO quiz : quizListDTO.getQuiz()) {
            quiz.setQuizListSeq(quizListDTO.getQuizListSeq());
            quiz.setCourseSeq(quizListDTO.getCourseSeq());
            quiz.setQuizOrder(quizOrder++);

            // 2. 퀴즈 등록
            quizMapper.insertQuiz(quiz);

            // 3. 보기 등록
            int optionOrder = 1;
            for (QuizOptionDTO option : quiz.getOptions()) {
                option.setQuizSeq(quiz.getQuizSeq());
                option.setOptionOrder(optionOrder++);

                if (option.getFileIndex() != null && imageFiles != null) {
                    MultipartFile file = imageFiles.get(option.getFileIndex());

                    // 확장자 추출 및 검증
                    String originalName = file.getOriginalFilename();
                    String ext = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
                    if (!List.of("jpg", "jpeg", "png", "gif").contains(ext)) {
                        throw new IllegalArgumentException("지원되지 않는 이미지 형식입니다.");
                    }

                    // 파일 이름 랜덤 생성 (같은 사진 중복 저장 가능)
                    String uuid = UUID.randomUUID().toString();
                    String fileName = uuid + "_" + System.currentTimeMillis() + "." + ext;

                    // 저장 경로 설정
                    File savePath = new File(saveDir, fileName);
//                    if (!savePath.getParentFile().exists()) {
//                        savePath.getParentFile().mkdirs();
//                    }
                    try {
                    	file.transferTo(savePath);
                    } catch (IOException e) {
                    	// 예외 던지기
                    	throw new IOException("이미지 파일 저장 실패: " + originalName, e); 
                    }
                    
                    option.setImageName(fileName);
                    option.setImageAddr(saveDir);
                }

                quizMapper.insertQuizOption(option);
            }
        }
    }//addQuiz
    
    
    //퀴즈 가져오기
    @Override
    public Map<String, Object> getQuizList(String quizListSeq, String userSeq) {
       
    	Map<String, Object> result = new HashMap<>();

    	//퀴즈 목록 조회
        List<QuizDTO> quizList = quizMapper.selectQuizWithOptions(quizListSeq);
        System.out.println("QuizList: " + quizList); 

        // 정답 수, 응답 수 조회
        QuizStatusDTO quizStatus = new QuizStatusDTO();
        quizStatus.setQuizListSeq(quizListSeq);
        quizStatus.setUserSeq(userSeq);

        QuizStatusDTO qsDTO = quizMapper.QuizCorrectChk(quizStatus);
        int totalQuizCnt = quizList.size(); //총 퀴즈 수
        int responseCnt = qsDTO.getTotalCnt();   //사용자의 응답 수
        int correctCnt = qsDTO.getCorrectCnt();  //맞힌 정답 수
//        // 사용자 응답 개수 조회
//        int responseCnt = quizMapper.countUserResponse(quizListSeq, userSeq);
        
        // 상태 판단 (학습전/학습중(중도포기)/학습완료)
        String status;
        if (responseCnt == 0) {
            status = "학습전";
        } else if (responseCnt < totalQuizCnt) {
            status = "학습중";
        } else if (responseCnt == totalQuizCnt) {
            status = "학습완료";
        } else {
        	//예외 상황: 중복 저장같은 버그로 응답 수가 총 퀴즈 수보다 더 많아질 경우
         throw new IllegalStateException("응답 수: "+responseCnt + " > 총 퀴즈 수: " + totalQuizCnt);
        }
        
        //진행률 계산 (프로그레스 바)
        double progress = (double) responseCnt / totalQuizCnt * 100;
        
        //Map<String, Object> result
        result.put("userSeq", userSeq); //유저별 응답 저장
        result.put("quizListSeq", quizListSeq);
        result.put("quizList", quizList);
        result.put("status", status);
        result.put("progress", progress);
        result.put("correctCnt", correctCnt);
        

        return result;
    }//getQuizList

    //퀴즈 응답 저장하기 (최초 응답만 저장)
    @Override
    public void saveQuizResponse(QuizResponseDTO qrDTO){
    	
    	//중복 응답 체크 true = insert 방지, false 로직 실행
    	boolean ResponseCheck = quizMapper.existsQuizResponse(qrDTO.getQuizSeq(),qrDTO.getUserSeq());
    	
    	if (ResponseCheck) {
    		return; 
		}//end if
    	
    	//퀴즈 조회
    	QuizDTO quiz=quizMapper.selectQuiz(qrDTO.getQuizSeq());
    	
    	//정답:Y 오답:N 체크
    	boolean correct = qrDTO.getSelectOption() == quiz.getCorrectOption();
    	qrDTO.setCorrectCheck(correct ? "Y" : "N");
    	
    	//상태 저장 
    	qrDTO.setCourseSeq(quiz.getCourseSeq());
    	qrDTO.setQuizListSeq(quiz.getQuizListSeq());
    	
    	//DB에 저장
    	quizMapper.insertQuizResponse(qrDTO);
    	System.out.println("✔️ 응답 저장 시 quizListSeq = " + qrDTO.getQuizListSeq());
    }//saveQuizResponse
    
    //퀴즈 학습완료 화면
    @Override
    public Map<String, Object> getQuizCompletionStats(String userSeq, String quizListSeq) {
       
    	QuizStatusDTO status = quizMapper.QuizCorrectChk(new QuizStatusDTO(userSeq, quizListSeq));

        int totalCnt = status.getTotalCnt();
        int correctCnt = status.getCorrectCnt();
        int rate = totalCnt == 0 ? 0 : (int)((correctCnt * 100.0) / totalCnt);

        Map<String, Object> result = new HashMap<>();
        result.put("totalCnt", totalCnt);
        result.put("correctCnt", correctCnt);
        result.put("accuracyRate", rate);
        return result;
    }
  
    //퀴즈 수정할때 db 정보 불러오기
    @Override
    public QuizListDTO getQuizListForModify(String quizListSeq) {
        // 1. 퀴즈 묶음 기본 정보 (제목, 언어 등)
        QuizListDTO quizListDTO = quizMapper.selectQuizListInfo(quizListSeq);

        // 2. 퀴즈 + 보기 전체 정보 세팅
        List<QuizDTO> quizList = quizMapper.selectQuizWithOptions(quizListSeq);
        quizListDTO.setQuiz(quizList);

        return quizListDTO;
    }
    
    //퀴즈 수정 update
    @Override
    @Transactional
    public void updateQuiz(String quizListSeq, String quizJson, List<MultipartFile> imageFiles) throws Exception {
    	  ObjectMapper mapper = new ObjectMapper();
    	    QuizListDTO parsed = mapper.readValue(quizJson, QuizListDTO.class);

    	    // 1. 퀴즈 리스트 메타 정보 수정
    	    quizMapper.updateQuizList(parsed.getTitle(), parsed.getLangCategory(), quizListSeq);

    	    // 2. 퀴즈 순회
    	    for (int i = 0; i < parsed.getQuiz().size(); i++) {
    	        QuizDTO quiz = parsed.getQuiz().get(i);
    	        quiz.setQuizListSeq(quizListSeq);
    	        quiz.setQuizOrder(i + 1);

    	        // 2-1. 퀴즈 정보 수정
    	        quizMapper.updateQuiz(quiz);

    	        // 2-2. 기존 보기 삭제
    	        quizMapper.deleteOptionsByQuizSeq(quiz.getQuizSeq());

    	        // 2-3. 보기 다시 등록
    	        int optionOrder = 1;
    	        for (QuizOptionDTO option : quiz.getOptions()) {
    	            option.setQuizSeq(quiz.getQuizSeq());
    	            option.setOptionOrder(optionOrder++);

    	            // 새 이미지가 선택된 경우
    	            if (option.getFileIndex() != null && imageFiles != null && option.getFileIndex() < imageFiles.size()) {
    	                MultipartFile file = imageFiles.get(option.getFileIndex());

    	                String originalName = file.getOriginalFilename();
    	                String ext = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
    	                if (!List.of("jpg", "jpeg", "png", "gif").contains(ext)) {
    	                    throw new IllegalArgumentException("지원되지 않는 이미지 형식입니다.");
    	                }

    	                String uuid = UUID.randomUUID().toString();
    	                String fileName = uuid + "_" + System.currentTimeMillis() + "." + ext;

    	                File savePath = new File(saveDir, fileName);
    	                file.transferTo(savePath);

    	                option.setImageName(fileName);
    	                option.setImageAddr(saveDir);
    	            }
    	            // 새 이미지 업로드 안 한 경우, 기존 정보 유지
    	            else if (option.getImageName() != null && !option.getImageName().isBlank()) {
    	                option.setImageName(option.getImageName());
    	                option.setImageAddr(saveDir);
    	            } else {
    	                // 완전히 삭제된 경우
    	                option.setImageName(null);
    	                option.setImageAddr(null);
    	            }

    	            quizMapper.insertQuizOption(option);
    	        }
    	}
    }
    
    //퀴즈 soft delete
    @Override
    public void softDeleteAllQuiz(String quizListSeq) {
    	quizMapper.softDeleteQuizList(quizListSeq);
    	quizMapper.softDeleteQuiz(quizListSeq);
    	quizMapper.softDeleteOptions(quizListSeq);
    }

	@Override
	public List<QuizListDTO> searchQuizByCourseSeq(String courseSeq) {

		
		return quizMapper.selectQuizByCourseSeq(courseSeq);
	}

	@Override
	public List<QuizListDTO> searchQuizSeqByCoursSEq(String courseSeq) {

		return quizMapper.selectQuizSeqByCourseId(courseSeq);
	}

	@Override
	public List<QuizListDTO> searchDistinctQuizLists(String courseSeq) {
		return quizMapper.selectDistinctQuizLists(courseSeq);
	}
    
    //퀴즈 정답 알려주기
//    @Override
//    public Map<String, Object> processSubmitAnswer(QuizResponseDTO qrDTO) {
//        // 정답 여부 판별 + 응답 저장
//        QuizDTO quiz = quizMapper.selectQuiz(qrDTO.getQuizSeq());
//        boolean isCorrect = qrDTO.getSelectOption() == quiz.getCorrectOption();
//
//        qrDTO.setCorrectCheck(isCorrect ? "Y" : "N");
//        qrDTO.setCourseSeq(quiz.getCourseSeq());
//        qrDTO.setQuizListSeq(quiz.getQuizListSeq());
//
//        quizMapper.insertQuizResponse(qrDTO); 
//
//        // 정답 보기 내용
//        String correctContent = quizMapper.selectCorrectContent(qrDTO.getQuizSeq());
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("correct", isCorrect);
//        result.put("correctOption", quiz.getCorrectOption());
//        result.put("correctContent", correctContent);
//
//        return result;
//    }
    
}//class