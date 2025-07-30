package kr.co.sist.e_learning.support;

import java.util.List;

public interface SupportService {

	public List<FaqDTO> searchTop3Faq();

	public boolean addNotice(NoticeDTO dto);// 단일 Notice 추가

	public boolean addFaq(FaqDTO dto);// 단일 Faq 추가

	public boolean addFeedback(FeedbackDTO dto);// 단일 Feedback 추가

	// ---------------------------------------------------------------------

	public List<NoticeDTO> searchAllNotice();// 전체 Notice 조회

	public List<NoticeDTO> searchAllNoticeAdmin(); // 전체 Notice 조회(관리자)

	public List<FaqDTO> searchAllFaq();// 전체 Faq 조회

	public List<FeedbackDTO> searchAllFeedback();// 전체 Feedback 조회

	public List<FeedbackDTO> searchTotalFeedback();// 전체 Feedback 조회

	public int editFaqStatusInactive(String id);
	// ---------------------------------------------------------------------

	public NoticeDTO searchOneNotice(String id);// 상세 Notice 조회

	public FaqDTO searchOneFaq(String id);// 상세 FaqDTO 조회

	public FeedbackDTO searchOneFeedback(String id);// 상세 Feedback 조회

	// ---------------------------------------------------------------------

	public boolean editNoticeTitle(String Id, String newTitle);

	public boolean editNoticeContent(String Id, String newContent);

	public boolean editNoticeFixFlag(String Id, String fixFlag);

	public boolean editNoticeHit(String id);

	public int editNoticeStatusInactive(String id);// delete 대체=비활성

	public int editNoticeStatusActive(String id);// delete 대체=활성

	// -----

	public boolean editFaqTitle(String id, String newTitle);

	public boolean editFaqContent(String id, String newContent);

	public boolean editFaqtype(String id, String type);

	public boolean editFaqHit(String id);

	public boolean editFaqStatus(String id);// delete 대체

	// -----

	public boolean editFeedbackStatus(String id);

	public boolean editFeedbackStep(String id);

	public boolean editFeedbackRefuse(String id);

	public boolean editFeedbackStepFinished(String id);

	// ---------------------------------------------------------------------

	public boolean respondToFaq(String noticeId, String responseType); // 해당 faq에 대한 피드백

	public boolean saveMarkFaq(String userId, String faqId);// 해당 faq 즐겨찾기 추가

	public boolean saveUnfinishedFeedback(String userId, String feedbackStatus);// 피드백 임시저장

	public boolean notifyFeedbackProgress(String feedbackId, String userId); // 피드백 진척도 이메일 안내

	public boolean suggestBetterKeywords(String rawKeyword); // 지원센터 통합검색 키워드 제안

	public boolean switctSupportCenterMode(String mode);// 사용자 맞춤형 지원센터 모드변경

	// ---------------------------------------------------------------------

	public List<String> findTitlesByKeyword(String keyword);

	public NoticeDTO findOneByTitle(String title);

	public List<NoticeDTO> findNoticesByKeyword(String keyword);

	public List<FaqDTO> searchFaqsByType(String typeId);

	public List<FaqDTO> searchAllFaqDTO();

	public List<FaqtypeDTO> selectActiveFaqTypes();

	public List<FaqDTO> selectFaqByType(String typeId);

	public List<FaqtypeDTO> searchAllFaqtype();

	public List<FeedbackDTO> searchAllNewFeedback();

	public List<FeedbackDTO> searchAllNiceFeedback();

	public int searchMaxFaqHits();

	public int searchMaxFeedbackSends();

}
