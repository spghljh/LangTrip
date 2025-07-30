package kr.co.sist.e_learning.admin.log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;

@Service
public class AdminLogServiceImpl implements AdminLogService {

    @Autowired
    private AdminLogDAO adminLogDAO;

    @Override
    public void addLog(AdminLogDTO logDTO) {
        adminLogDAO.insertLog(logDTO);
    }

    @Override
    public PageResponseDTO<AdminLogDTO> getAdminLogs(PageRequestDTO pageRequestDTO, AdminLogDTO searchDTO) {
        Map<String, Object> param = new HashMap<>();
        param.put("offset", pageRequestDTO.getOffset());
        param.put("limit", pageRequestDTO.getLimit());
        param.put("orderBy", pageRequestDTO.getOrderBy());
        param.put("sort", pageRequestDTO.getSort());
        param.put("searchKeyword", searchDTO.getSearchKeyword());
        param.put("searchType", searchDTO.getSearchType());
        param.put("startDate", searchDTO.getStartDate());
        param.put("endDate", searchDTO.getEndDate());

        List<AdminLogDTO> logs = adminLogDAO.selectAdminLogs(param);
        int totalCount = adminLogDAO.countAdminLogs(param);

        return new PageResponseDTO<>(logs, totalCount, pageRequestDTO.getPage(), pageRequestDTO.getSize(), 5);
    }

    @Override
    public List<AdminLogDTO> getAllAdminLogs(AdminLogDTO searchDTO) {
        Map<String, Object> param = new HashMap<>();
        param.put("searchKeyword", searchDTO.getSearchKeyword());
        param.put("searchType", searchDTO.getSearchType());
        param.put("startDate", searchDTO.getStartDate());
        param.put("endDate", searchDTO.getEndDate());

        return adminLogDAO.selectAllAdminLogs(param);
    }
}
