package kr.co.sist.e_learning.usercourse;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserCourseServiceImpl implements UserCourseService{

	@Autowired
	private UserCourseMapper ucm;

	@Override
	public void addUserCourse(UserCourseDTO ucDTO) {
		ucm.insertUserCourse(ucDTO);
	}

	@Override
	public List<UserCourseDTO> searchUserCourseByUserId(String userId) {
		List<UserCourseDTO> list =  ucm.selectUserCoursesByUserId(userId);
		return list;
	}

	@Override
	public List<UserCourseDTO> searchUserCourseByPage(Map<String, Object> param) {
		
		return ucm.selectUserCourseByPage(param);
	}

	@Override
	public int searchUserCourseCount(String userId) {
		int userCourseCount=ucm.selectUserCourseCount(userId);
		return userCourseCount;
	}

	

}
