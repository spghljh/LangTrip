package kr.co.sist.e_learning.usercourse;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserCourseMapper {

	void insertUserCourse(UserCourseDTO ucDTO); // 사용자가 수강 눌렀을떄 사용자 수강 테이블에 추가

	public List<UserCourseDTO> selectUserCoursesByUserId(String userId);

	public List<UserCourseDTO> selectUserCourseByPage(Map<String, Object> paramMap);

	public int selectUserCourseCount(String userId); 
}
