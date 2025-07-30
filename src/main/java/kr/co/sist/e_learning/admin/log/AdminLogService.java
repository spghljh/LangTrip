package kr.co.sist.e_learning.admin.log;

import java.util.List;

import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;

public interface AdminLogService {
    public void addLog(AdminLogDTO logDTO);
    PageResponseDTO<AdminLogDTO> getAdminLogs(PageRequestDTO pageRequestDTO, AdminLogDTO searchDTO);

    List<AdminLogDTO> getAllAdminLogs(AdminLogDTO searchDTO);
}
