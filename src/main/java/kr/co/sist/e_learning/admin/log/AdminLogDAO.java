package kr.co.sist.e_learning.admin.log;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminLogDAO {
    public int insertLog(AdminLogDTO logDTO);
    public List<AdminLogDTO> selectAdminLogs(Map<String, Object> param);
    public int countAdminLogs(Map<String, Object> param);

    public List<AdminLogDTO> selectAllAdminLogs(Map<String, Object> param);
}
