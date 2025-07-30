package kr.co.sist.e_learning.community.service;

public interface CommunityReportService {
    /**
     * @param postId2     신고할 게시글 ID
     * @param reporterId  신고자 회원 ID
     * @param reasonChk   선택된 사유 코드 (1~4)
     * @param reasonText  추가 설명 (최대 20자)
     */
    void reportPost(Long postId2,
                    Long reporterId,
                    Integer reasonChk,
                    String reasonText);
}
