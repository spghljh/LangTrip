package kr.co.sist.e_learning.admin.auth;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminAuthDAO {
    AdminAuthDTO loginSelectAdminById(String id);
    List<String> selectAdminRoles(String adminId);
}
