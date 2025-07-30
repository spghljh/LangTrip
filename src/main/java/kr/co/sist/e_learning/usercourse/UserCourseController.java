package kr.co.sist.e_learning.usercourse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.e_learning.course.CourseDTO;
import kr.co.sist.e_learning.course.CourseService;
import kr.co.sist.e_learning.quiz.QuizListDTO;
import kr.co.sist.e_learning.quiz.QuizService;
import kr.co.sist.e_learning.video.VideoDTO;
import kr.co.sist.e_learning.video.VideoService;

@Controller
public class UserCourseController {

	@Autowired
	private UserCourseService ucs;
	
	@Autowired
	private CourseService cs;
	
	@Autowired
	private VideoService vs;
	
	@Autowired
	private QuizService qs;
	
	@GetMapping("/ui/user_lecture")
	public String userLecture(@RequestParam("seq") String courseSeq, Model model, HttpSession session) {
		CourseDTO cDTO = cs.selectCourseData(courseSeq);
		String userSeq = (String) session.getAttribute("user_seq");
    	if (userSeq == null) {
    	    userSeq = "test123";
    	    session.setAttribute("user_seq", userSeq); 
    	}
		List<VideoDTO> videoList = vs.searchVideoByCourseSeq(courseSeq);
//		List<QuizListDTO> quizList = qs.searchQuizByCourseSeq(courseSeq);
		List<QuizListDTO> quizSeq = qs.searchQuizSeqByCoursSEq(courseSeq);
		List<QuizListDTO> quizList = qs.searchDistinctQuizLists(courseSeq);
		
		model.addAttribute("courseData", cDTO);
		model.addAttribute("videoList", videoList);
		model.addAttribute("quizList", quizList);
		return "ui/user_lecture";
	}
	
	@GetMapping("/ui/user_course")
	public String userCourse(HttpSession session) {
		if(session.getAttribute("userId") == null) {
			session.setAttribute("userId", "test123");
		}
		System.out.println("user_course진입성공");
		
		return "ui/user_course";
	}
	
	@GetMapping("/user/showUserCourses")
	@ResponseBody
	public Map<String, Object> showUserCourse(HttpSession session,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "5") int limit){
		
		String userId = (String)session.getAttribute("userId");
		if(session.getAttribute("userId") == null) {
			session.setAttribute("userId", "test123");
		}
		
		int offset = (page -1)*limit;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("offset", offset);
		param.put("limit", limit);
		List<UserCourseDTO> paginationList = ucs.searchUserCourseByPage(param);
		
		int totalCount = ucs.searchUserCourseCount(userId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userCourseList", paginationList);
		result.put("totalCount", totalCount);
		result.put("page", page);
	    result.put("limit", limit);
		
		List<UserCourseDTO> list = ucs.searchUserCourseByUserId(userId);
		for(UserCourseDTO uDTO : list) {
			System.out.println(uDTO.toString());
		}
		
		result.put("courses", list);
		return result;
	}
	
	
	
	
}
