package kr.co.sist.e_learning.admin.course;

import kr.co.sist.e_learning.pagination.PageResponseDTO;

import java.util.Map;

public interface AdminCourseService {
    PageResponseDTO<AdminCourseDTO> getAdminCourses(Map<String, Object> params);
    AdminCourseDTO getAdminCourseDetail(String courseSeq);
    void updateCourseVisibility(String courseSeq, String isPublic);
}