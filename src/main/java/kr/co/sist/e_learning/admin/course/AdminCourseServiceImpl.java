package kr.co.sist.e_learning.admin.course;

import kr.co.sist.e_learning.pagination.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminCourseServiceImpl implements AdminCourseService {

    @Autowired
    private AdminCourseMapper adminCourseMapper;

    @Override
    public PageResponseDTO<AdminCourseDTO> getAdminCourses(Map<String, Object> params) {
        int page = (int) params.get("page");
        int pageSize = (int) params.get("pageSize");

        List<AdminCourseDTO> list = adminCourseMapper.selectAdminCourses(params);
        int totalCount = adminCourseMapper.countAdminCourses(params);

        return new PageResponseDTO<>(list, totalCount, page, pageSize, 5);
    }

    @Override
    public AdminCourseDTO getAdminCourseDetail(String courseSeq) {
        return adminCourseMapper.selectAdminCourseDetail(courseSeq);
    }

    @Override
    public void updateCourseVisibility(String courseSeq, String isPublic) {
        adminCourseMapper.updateCourseVisibility(courseSeq, isPublic);
    }
}