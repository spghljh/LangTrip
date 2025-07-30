package kr.co.sist.e_learning.adminDash;

import java.util.List;

public interface AdminDashService {
    List<AdminDashDTO> getDailySignupStats();
    List<AdminDashDTO> getSignupPathStats();
    List<AdminDashDTO> getUnsignReasonStats();
}
