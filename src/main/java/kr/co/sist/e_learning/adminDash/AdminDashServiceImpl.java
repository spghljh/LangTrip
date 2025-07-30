package kr.co.sist.e_learning.adminDash;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@Primary
public class AdminDashServiceImpl implements AdminDashService {

    @Autowired
    private AdminDashDAO adDAO;

    @Override
    public List<AdminDashDTO> getDailySignupStats() {
        List<AdminDashDTO> signList = adDAO.getDailySignupStats();
        System.out.println("일별 가입자 수: " + signList);
        return signList;
    }

    @Override
    public List<AdminDashDTO> getSignupPathStats() {
        List<AdminDashDTO> pathList = adDAO.getSignupPathStats();
        System.out.println("유입 경로 통계: " + pathList);
        return pathList;
    }

    @Override
    public List<AdminDashDTO> getUnsignReasonStats() {
        List<AdminDashDTO> reasonList = adDAO.getUnsignReasonStats();
        System.out.println("탈퇴 사유 통계: " + reasonList);
        return reasonList;
    }
}
