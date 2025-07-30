package kr.co.sist.e_learning.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.e_learning.community.dao.CommunityReportDAO;
import kr.co.sist.e_learning.community.dto.CommuRpModal;
import kr.co.sist.e_learning.community.dto.CommunityReportDTO;

@Service
public class CommunityReportServiceImpl implements CommunityReportService {

    @Autowired
    private CommunityReportDAO reportDAO;

    @Autowired
    private CommunityReportDAO reasonDAO;

    @Override
    public void reportPost(Long postId2,
                           Long reporterId,
                           Integer reasonChk,
                           String reasonText) {

        System.out.println("[Service] 신고 서비스 진입");
        System.out.println("[Service] postId2=" + postId2
                         + ", reporterId=" + reporterId
                         + ", reasonChk=" + reasonChk
                         + ", reasonText=" + reasonText);

        boolean alreadyReported = reportDAO.hasReportedToday(postId2, reporterId);
        System.out.println("[Service] 중복 신고 여부: " + alreadyReported);

        if (alreadyReported) {
            throw new IllegalStateException("ALREADY_REPORTED");
        }

        CommunityReportDTO dto = new CommunityReportDTO();
        dto.setPostId2(postId2);
        dto.setReporterId(reporterId);
        dto.setReasonText(reasonText);
        
        reportDAO.insertReport(dto);
        Long newReportId = dto.getReportId();
        System.out.println("[Service] insert 후 생성된 reportId: " + newReportId);

        reasonDAO.insertReason(newReportId, reasonChk);
        System.out.println("[Service] 신고 사유 insert 완료");
    }
}


