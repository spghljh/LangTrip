package kr.co.sist.e_learning.adminDash;

import java.util.List;
import kr.co.sist.e_learning.adminDash.AdminDashDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDashDAO {
    List<AdminDashDTO> getDailySignupStats();
    List<AdminDashDTO> getSignupPathStats();
    List<AdminDashDTO> getUnsignReasonStats();
}
