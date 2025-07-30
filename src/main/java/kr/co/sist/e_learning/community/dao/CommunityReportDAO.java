package kr.co.sist.e_learning.community.dao;

import kr.co.sist.e_learning.community.dto.CommunityReportDTO;

public interface CommunityReportDAO {
    /** report 테이블에 기본 신고 정보 INSERT */
    void insertReport(CommunityReportDTO dto);

    /** 오늘 해당 글을 이미 신고했는지 체크 */
    boolean hasReportedToday(Long postId2, Long reporterId);

    /** REPORT_REASON_USER 테이블에 사유 코드 INSERT */
    void insertReason(Long reportId, Integer reasonChk);
}
