package kr.co.sist.e_learning.admin.course;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminCourseMapper {
    List<AdminCourseDTO> selectAdminCourses(Map<String, Object> params);
    int countAdminCourses(Map<String, Object> params);
    AdminCourseDTO selectAdminCourseDetail(@Param("courseSeq") String courseSeq);
    void updateCourseVisibility(@Param("courseSeq") String courseSeq, @Param("isPublic") String isPublic);
}