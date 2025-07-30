package kr.co.sist.e_learning.admin.account;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminAccountDAO {
	void updateAdmin(AdminAccountUnifiedDTO dto);

    // 기존 기능
    List<String> selectRolesByAdminId(String adminId);
    void deleteAdminRoles(String adminId);
    void insertAdminRoles(Map<String, Object> params);

    // ✅ 신규 추가 기능
    AdminAccountUnifiedDTO selectUnifiedById(String id); // 가입신청 or 관리자 통합 조회
    void insertAdmin(AdminAccountUnifiedDTO dto); // 승인 시 관리자 테이블 등록
    void updateSignupStatus(String requestId, String status); // 가입신청 상태 업데이트
    List<AdminAccountUnifiedDTO> selectUnifiedAdminList(Map<String, Object> params);
    int countUnifiedAdmins(Map<String, Object> params);
    AdminAccountUnifiedDTO selectByAdminId(String adminId);     // 기존 관리자 조회
    AdminAccountUnifiedDTO selectByRequestId(String requestId); // 가입 신청자 조회
    List<String> selectRolesByRequestId(String requestId);
    void updateSignupStatusWithReason(
        @Param("requestId") String requestId,
        @Param("status") String status,
        @Param("reason") String reason
    );
    
    // 부서 목록 조회
    List<String> selectDistinctDepts();
}
