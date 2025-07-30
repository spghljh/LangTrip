package kr.co.sist.e_learning.admin.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;

@Controller
public class AdminLogController {

    @Autowired
    private AdminLogService adminLogService;

    @GetMapping("/admin/log")
    public String adminLogPage(@RequestParam(name = "pageSize", defaultValue = "10") int pageSize, PageRequestDTO pageRequestDTO, AdminLogDTO searchDTO, 
                               @RequestParam(name = "async", required = false) boolean async, Model model) {
        pageRequestDTO.setSize(pageSize); // Set the size from the request parameter
        PageResponseDTO<AdminLogDTO> pageResponse = adminLogService.getAdminLogs(pageRequestDTO, searchDTO);
        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("searchDTO", searchDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        
        if (async) {
            return "admin/log/admin_log_fragment";
        }
        
        return "admin/log/admin_log";
    }
}