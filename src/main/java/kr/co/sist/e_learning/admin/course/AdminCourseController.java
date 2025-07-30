package kr.co.sist.e_learning.admin.course;

import kr.co.sist.e_learning.pagination.PageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    @Autowired
    private AdminCourseService adminCourseService;

    @GetMapping
    public String listAdminCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false, defaultValue = "uploadDate,desc") String sort,
            @RequestParam(required = false) String isPublic,
            @RequestParam(name = "async", required = false) boolean async,
            Model model) {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("pageSize", pageSize);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        params.put("sort", sort);
        params.put("isPublic", isPublic);
        params.put("offset", (page - 1) * pageSize);
        params.put("limit", pageSize);

        PageResponseDTO<AdminCourseDTO> responseDTO = adminCourseService.getAdminCourses(params);

        model.addAttribute("courseList", responseDTO.getList());
        model.addAttribute("currentPage", responseDTO.getPage());
        model.addAttribute("totalPages", responseDTO.getTotalPages());
        model.addAttribute("startPage", responseDTO.getStartPage());
        model.addAttribute("endPage", responseDTO.getEndPage());
        model.addAttribute("totalCount", responseDTO.getTotalCnt());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("sort", sort);
        model.addAttribute("isPublic", isPublic);

        if (async) {
            return "admin/course/course_list_fragment";
        }

        return "admin/course/course_list";
    }

    @GetMapping("/{courseSeq}")
    public String adminCourseDetail(@PathVariable String courseSeq, Model model) {
        AdminCourseDTO course = adminCourseService.getAdminCourseDetail(courseSeq);
        model.addAttribute("course", course);
        return "admin/course/course_detail";
    }

    @PostMapping("/{courseSeq}/toggleVisibility")
    public String toggleCourseVisibility(@PathVariable String courseSeq, @RequestParam String isPublic, RedirectAttributes ra) {
        adminCourseService.updateCourseVisibility(courseSeq, isPublic);
        ra.addFlashAttribute("message", "Course visibility updated successfully!");
        return "redirect:/admin/courses/" + courseSeq;
    }
}