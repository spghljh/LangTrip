package kr.co.sist.e_learning.course;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

public interface CourseService {
	
	void addCourse(CourseDTO cDTO);
	public List<CourseDTO> selectCourse(String userId);
	public CourseDTO selectCourseData(String courseSeq);
	public List<CourseDTO> selectCourseByPage(Map<String, Object> param);
	public int selectCourseCount(String userId);
	public int modifyCourse(CourseDTO cDTO);
	public int removeCourse(String courseSeq);
}
