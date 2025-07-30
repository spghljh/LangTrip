package kr.co.sist.e_learning.community.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.sist.e_learning.community.dto.CommunityReportDTO;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CommunityReportDAOImpl implements CommunityReportDAO {

    private static final String NS = "kr.co.sist.e_learning.community.dao.CommunityReportDAO";

    @Autowired
    private SqlSession session;

    @Override
    public void insertReport(CommunityReportDTO dto) {
        session.insert(NS + ".insertReport", dto);
    }

    @Override
    public boolean hasReportedToday(Long postId2, Long reporterId) {
        Map<String,Object> params = new HashMap<>();
        params.put("postId2", postId2);
        params.put("reporterId", reporterId);
        Integer cnt = session.selectOne(NS + ".hasReportedToday", params);
        return cnt != null && cnt > 0;
    }

    @Override
    public void insertReason(Long reportId, Integer reasonChk) {
        Map<String,Object> params = new HashMap<>();
        params.put("reportId", reportId);
        params.put("reasonChk", reasonChk);
        session.insert(NS + ".insertReason", params);
    }
}
