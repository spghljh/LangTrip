package kr.co.sist.e_learning.admin.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.sist.e_learning.common.aop.Loggable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/accounts")
public class AdminAccountController {

    @Autowired
    private AdminAccountService adminAccountService;


    /**
     * 관리자 상세 or 가입신청 상세
     */
    @GetMapping("/{id}")
    public String getAdminOrSignupDetail(@PathVariable("id") String id, Model model) {
        AdminAccountUnifiedDTO dto = adminAccountService.getById(id);

        String mode = "PENDING".equals(dto.getStatus()) ? "signup" : "account";
        model.addAttribute("mode", mode);
        model.addAttribute("admin", dto);
        model.addAttribute("roleNameMap", getRoleNameMap());
        model.addAttribute("depts", adminAccountService.getAllDepts());

        return "admin/account/account_detail";
    }


    /**
     * 관리자 정보 수정
     */
    @PostMapping("/{id}")
    @Loggable(actionType = "ADMIN_UPDATE")
    public String updateAdmin(@PathVariable("id") String id,
                              @ModelAttribute AdminAccountUnifiedDTO dto) {
        dto.setAdminId(id);
        adminAccountService.updateAdmin(dto);
        return "redirect:/admin/accounts/" + id;
    }

    /**
     * 가입신청 상태 변경 (승인/거절)
     */
    @PostMapping("/{requestId}/status")
    public String updateSignupStatus(@PathVariable String requestId,
                                     @RequestParam String status,
                                     @RequestParam(required = false) String reason,
                                     RedirectAttributes redirect) {
        if ("APPROVED".equals(status)) {
            adminAccountService.approveSignup(requestId);
            redirect.addFlashAttribute("message", "가입 요청이 승인되었습니다.");
        } else if ("REJECTED".equals(status)) {
            adminAccountService.rejectSignup(requestId, reason);
            redirect.addFlashAttribute("message", "가입 요청이 거절되었습니다.");
        }
        return "redirect:/admin/accounts";
    }
    /**
     * 권한명 Map
     */
    private Map<String, String> getRoleNameMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ROLE_SUPER", "슈퍼관리자");
        map.put("ROLE_ACCOUNT", "계정관리");
        map.put("ROLE_COURSE", "강의관리");
        map.put("ROLE_PAYMENT", "결제관리");
        map.put("ROLE_SUPPORT", "고객지원");
        map.put("ROLE_MEMBER", "회원관리");
        map.put("ROLE_SUBSCRIBE", "구독관리");
        map.put("ROLE_REPORT", "신고관리");
        map.put("ROLE_COMMUNITY", "커뮤니티관리");
        return map;
    }
    
    @GetMapping
    public String listUnifiedAccounts(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String searchType,
        @RequestParam(required = false) String searchKeyword,
        @RequestParam(required = false, defaultValue = "createdAt,desc") String sort,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam(name = "async", required = false) boolean async,
        Model model
    ) {
        int offset = (page - 1) * pageSize;

        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);
        params.put("sort", sort);
        params.put("offset", offset);
        params.put("limit", pageSize);

        List<AdminAccountUnifiedDTO> list = adminAccountService.getUnifiedAdminList(params);
        int totalCount = adminAccountService.getUnifiedAdminTotalCount(params);

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int blockSize = 5;
        int startPage = Math.max(1, page - blockSize / 2);
        int endPage = Math.min(totalPages, startPage + blockSize - 1);
        if (endPage - startPage < blockSize - 1) {
            startPage = Math.max(1, endPage - blockSize + 1);
        }

        model.addAttribute("adminList", list);
        model.addAttribute("status", status);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("page", page);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("roleNameMap", getRoleNameMap());

        if (async) {
            return "admin/account/account_list_fragment";
        }

        return "admin/account/account_list";
    }

}
