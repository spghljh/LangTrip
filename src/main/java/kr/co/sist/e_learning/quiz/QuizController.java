package kr.co.sist.e_learning.quiz;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
public class QuizController {

    @Autowired
    private QuizService quizService;

    // 강의실 이동 
//    @GetMapping("/quiz/classRoom")
//    public String showClassRoom(HttpSession session, Model model) {
//    	// userSeq  
//    	String userSeq = (String) session.getAttribute("user_seq");
//    	if (userSeq == null) {
//    	    userSeq = "user001";
//    	    session.setAttribute("user_seq", userSeq); 
//    	}
//        
//        // 전체 퀴즈 묶음 목록 조회
//        List<QuizListDTO> quizList = quizService.getAllQuizList(userSeq); 
//
//        model.addAttribute("quizList", quizList);
//        
//        return "quiz/classRoom"; 
//    }
    
    // 강의실 : 퀴즈 시작 전 title/language modal창 전용
    @GetMapping("/classRoom/info/data")
    @ResponseBody
    public QuizListDTO QuizModal(@RequestParam String quizListSeq) {
        return quizService.getQuizListInfo(quizListSeq);
    }
    
    // 퀴즈 등록 폼 
    @GetMapping("/quiz/addQuizForm")
    public String showAddQuizForm(@RequestParam("seq") String courseSeq, 
    		Model model) {
    	model.addAttribute("courseSeq", courseSeq);
    	
        return "quiz/addQuizForm"; 
    }

    @Value("${upload.saveDirQuiz}")
    private String saveDir;
    
    // 퀴즈 등록 처리
    @ResponseBody
    @PostMapping("/addQuizForm")
    public String addQuiz(
        @RequestPart("quizJson") String quizJson,
        @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles
    ) throws Exception {

    	System.out.println("📂 저장 경로: " + saveDir);
    	
    	//서비스로 옮기기
        // JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        QuizListDTO quizListDTO = mapper.readValue(quizJson, QuizListDTO.class);
        System.out.println(quizListDTO.getCourseSeq());
        // 이미지 저장 처리
        for (QuizDTO quiz : quizListDTO.getQuiz()) {
            for (QuizOptionDTO option : quiz.getOptions()) {
                Integer fileIndex = option.getFileIndex();

                //사용자가 업로드한 파일 리스트에서 fileIndex로 해당 이미지 가져오기
                if (fileIndex != null && imageFiles != null && fileIndex < imageFiles.size()) {
                    MultipartFile img = imageFiles.get(fileIndex);
                 
                    
                    // 파일명 생성 (UUID + 타임스탬프로 파일명 생성해서 중복 방지)
                    String originName = img.getOriginalFilename();
                    String ext = originName.substring(originName.lastIndexOf(".") + 1).toLowerCase();
                    String uuid = UUID.randomUUID().toString();
                    String fileName = uuid + "_" + System.currentTimeMillis() + "." + ext;
                    
                    
                    File dir = new File(saveDir);
                    if (!dir.exists()) {
                        dir.mkdirs();  // 디렉토리가 없으면 생성
                    }

                    // 실제 파일 저장
                    File saveFile = new File(saveDir, fileName);
                    img.transferTo(saveFile);
                    
                    
                    // DTO에 이미지 정보 세팅
                    option.setImageName(fileName);
                    option.setImageAddr(saveDir);
                }
            }
        }

        // DB 저장 서비스 호출
        quizService.addQuiz(quizListDTO, imageFiles);
        
        return "success";
    }//addQuiz
   
    // 퀴즈 학습 페이지
    @GetMapping("/quiz/playQuiz/{quizListSeq}")
    public String showPlayQuiz(@PathVariable String quizListSeq, Model model,
    		@RequestParam String courseSeq) {
    	System.out.println("✅ 퀴즈 학습 진입! quizListSeq: " + quizListSeq);
        model.addAttribute("quizListSeq", quizListSeq);
        model.addAttribute("courseSeq", courseSeq);
        return "quiz/playQuiz"; 
    }
    
    //퀴즈 시작
    @ResponseBody
    @GetMapping("/playQuizProcess/{quizListSeq}")
    public Map<String, Object> playQuiz( @PathVariable String quizListSeq,//퀴즈 묶음ID
    		HttpSession session//세션에서 사용자 정보 꺼내기
    		) {
    	
    	System.out.println("✅ 퀴즈 학습 시작!");
    	System.out.println(quizListSeq);
    	
    	String userSeq = (String) session.getAttribute("user_seq");
    	if (userSeq == null) {
    	    userSeq = "user001";
    	    session.setAttribute("user_seq", userSeq); 
    	}
    	
    	 
    	Map<String, Object> result=quizService.getQuizList(quizListSeq, userSeq);
    	
    	return result;
    }//playQuiz
    
    //퀴즈 응답 문제 모두 풀면 insert 
    @ResponseBody
    @PostMapping("/quizResponse")
    public void saveQuizResponse(@RequestBody QuizResponseDTO qrDTO,
    		HttpSession session){
    	
    	
    	String userSeq = (String) session.getAttribute("user_seq");
    	if (userSeq == null) {
    	    userSeq = "user001";
    	    session.setAttribute("user_seq", userSeq); // ✅ 키 통일
    	    System.out.println(userSeq);
    	    System.out.println(qrDTO.toString());
    	}
    	qrDTO.setUserSeq(userSeq);
        
    	System.out.println("📦 DTO 내용 확인: " + qrDTO);
    	System.out.println("✅ quizListSeq 확인: " + qrDTO.getQuizListSeq());
    	//정답 체크, 상태 설정, insert
    	quizService.saveQuizResponse(qrDTO);
    			
    }
    
    //퀴즈 학습완료 화면으로 이동
    @GetMapping("/quiz/quizCompleted/{quizListSeq}")
    public String quizCompleted(@PathVariable String quizListSeq, Model model
    		,HttpSession session, @RequestParam("courseSeq") String courseSeq) {
    	
    	System.out.println(courseSeq);
    	String userSeq = (String) session.getAttribute("user_seq");
    	if (userSeq == null) {
    	    userSeq = "user001";
    	    session.setAttribute("user_seq", userSeq); 
    	}
    	QuizListDTO qDTO = new QuizListDTO();
    	qDTO.setCourseSeq(courseSeq);
    	Map<String, Object> quizStats = quizService.getQuizCompletionStats(userSeq, quizListSeq);
    	model.addAttribute("courseSeq", courseSeq);
        model.addAllAttributes(quizStats);
        model.addAttribute("qDTO", qDTO);
        return "quiz/quizCompleted"; 
    }
    
    //퀴즈 수정 폼 보여주기
    @GetMapping("/quiz/modifyQuizForm/{quizListSeq}")
    public String showModifyQuizForm (HttpSession session, Model model,
    		  @PathVariable String quizListSeq,
    		  @RequestParam String courseSeq) {
    	
    	
    	String userSeq = (String) session.getAttribute("user_seq");
    	if (userSeq == null) {
    	    userSeq = "user001";
    	    session.setAttribute("user_seq", userSeq); 
    	}
    	
    	QuizListDTO qlDTO = quizService.getQuizListForModify(quizListSeq);
        QuizListDTO qDTO = new QuizListDTO();
    	qDTO.setCourseSeq(courseSeq);
    	model.addAttribute("qlDTO", qlDTO);
    	model.addAttribute("qDTO", qDTO);
    	
        return "quiz/modifyQuizForm";
    }
    
    //퀴즈 수정 처리
    @PostMapping("/modifyQuizForm")
    @ResponseBody
    public String modifyQuiz(
            @RequestParam("quizListSeq") String quizListSeq,
            @RequestParam("quizJson") String quizJson,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {

        try {
			quizService.updateQuiz(quizListSeq, quizJson, imageFiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return "success";
    }
    
    //퀴즈 soft delete
    @PostMapping("/deleteQuizList/{quizListSeq}")
    @ResponseBody
    public String deleteQuizList(@PathVariable String quizListSeq) {
        quizService.softDeleteAllQuiz(quizListSeq);
        return "delete";
    }
    
//    //퀴즈 정답,오답 처리
//    @PostMapping("/submitAnswer")
//    @ResponseBody
//    public Map<String, Object> submitAnswer(@RequestBody QuizResponseDTO qrDTO) {
//        return quizService.processSubmitAnswer(qrDTO);
//    } 
    
}//class


