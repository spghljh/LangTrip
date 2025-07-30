package kr.co.sist.e_learning.course;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CourseMapper {
	
	 void insertCourse(CourseDTO cDTO);
	 public List<CourseDTO> searchCourseById(String userId);
	 public CourseDTO searchCourseByCourseId(String courseSeq);
	 public List<CourseDTO> searchCourseByPage(Map<String, Object> paramMap);
	 public int searchCourseCount(String userId);
	 public int updateCourseByCourseSeq(CourseDTO cDTO);
	 public int deleteCourseByCourseSeq(String courseSeq);
}
