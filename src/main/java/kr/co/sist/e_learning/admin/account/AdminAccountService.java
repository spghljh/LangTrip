package kr.co.sist.e_learning.admin.account;

import java.util.List;
import java.util.Map;

import kr.co.sist.e_learning.common.aop.Loggable;

public interface AdminAccountService {


    void updateAdmin(AdminAccountUnifiedDTO  dto);

    List<AdminAccountUnifiedDTO> getUnifiedAdminList(String status, String searchType, String searchKeyword, String sort);

    // 🔥 신규 추가
    AdminAccountUnifiedDTO getById(String id); // status 보고 분기
    void approveSignup(String requestId);

    @Loggable(actionType = "SIGNUP_REJECT")
    void rejectSignup(String requestId, String reason);
    List<AdminAccountUnifiedDTO> getUnifiedAdminList(Map<String, Object> params);
    int getUnifiedAdminTotalCount(Map<String, Object> params);
    List<String> getAllDepts();
}
