package kr.co.sist.e_learning.support;

import java.util.List;

public interface SupportDAO {

	boolean insertNoticeDTO(NoticeDTO dto);

	List<NoticeDTO> selectAllNoticeDTO();

	List<NoticeDTO> selectAllNoticeDTOAdmin();

	int updateNoticeById(NoticeDTO notice);

	boolean increaseNoticeHits(String id);

	boolean updateNoticeTitle(String id, String newTitle);

	boolean updateNoticeContent(String id, String newContent);

	void updateNoticeFixFlag(String id, String fixFlag);

	List<NoticeDTO> selectNoticeTitlesByKeyword(String keyword);

	NoticeDTO selectOneNoticeByTitle(String title);

	List<NoticeDTO> selectNoticesByKeyword(String keyword);

	int updateNoticeStatusInactive(String id);// delete 대체=비활성(완료)

	int updateFaqStatusInactive(String id);// delete 대체=비활성(완료)

	int updateNoticeStatusActive(String id);// delete 대체=활성(완료)

	NoticeDTO selectOneNoticeById(String id);

	// ---------------------------------------------------------------------------------------------------------------------

	boolean insertFaqDTO(FaqDTO dto);

	List<FaqDTO> selectAllFaqDTO();

	FaqDTO selectOneFaqById(String id);

	int updateFaqById(FaqDTO faq);

	boolean increaseFaqHits(String id);// (완료)

	public List<FaqDTO> selectFaqsByType(String typeId);

	boolean updateFaqTitle(String id, String newTitle);

	boolean updateFaqContent(String id, String newContent);

	boolean updateFaqtype(String id, String faqtype);

	List<FaqDTO> selectFaqByType(String typeId);

	List<FaqtypeDTO> selectActiveFaqTypes();

	List<FaqDTO> selectTop3FaqDTO();

	List<FaqDTO> selectFaqByTypeId(String typeId);

	int updateFaqStatus(String id);// delete 대체(완료)

	List<FaqtypeDTO> selectAllFaqtype();

	// ---------------------------------------------------------------------------------------------

	boolean insertFeedbackDTO(FeedbackDTO dto);

	List<FeedbackDTO> selectAllFeedbackDTO();

	List<FeedbackDTO> selectTotalFeedbackDTO();

	FeedbackDTO selectOneFeedbackById(String id);

	boolean updateFeedbackStatus(String id);// delete 대체(완료)

	boolean updateFeedbackStep(String feedback_id);

	boolean updateFeedbackStepRefuse(String feedback_id);

	boolean updateFeedbackStepFinished(String feedback_id);

	List<FeedbackDTO> selectAllNewFeedbackDTO();

	List<FeedbackDTO> selectAllNiceFeedbackDTO();

	int selectMaxFaqHits();

	int selectMaxFeedbackSends();

	List<FaqDTO> selectFaqsByKeyword(String keyword);

}
