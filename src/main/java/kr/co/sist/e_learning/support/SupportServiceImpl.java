package kr.co.sist.e_learning.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SupportServiceImpl extends SupportServiceAbstract {

	private final NoticeDTO noticeDTO;

	private final SupportDAO sDAO;

	public SupportServiceImpl(SupportDAO sDAO, NoticeDTO noticeDTO) {
		this.sDAO = sDAO;
		this.noticeDTO = noticeDTO;
	}
	// ---------------------------------------------------------------------

	@Override
	public boolean addNotice(NoticeDTO dto) {
		return sDAO.insertNoticeDTO(dto);
	}

	@Override
	public boolean addFaq(FaqDTO dto) {
		return sDAO.insertFaqDTO(dto);
	}

	@Override
	public boolean addFeedback(FeedbackDTO dto) {
		return sDAO.insertFeedbackDTO(dto);
	}

	// ---------------------------------------------------------------------

	@Override
	public List<NoticeDTO> searchAllNotice() {
		return sDAO.selectAllNoticeDTO();
	}

	@Override
	public List<NoticeDTO> searchAllNoticeAdmin() {
		return sDAO.selectAllNoticeDTOAdmin();
	}

	@Override
	public List<FaqDTO> searchAllFaq() {
		return sDAO.selectAllFaqDTO();
	}

	@Override
	public List<FeedbackDTO> searchAllFeedback() {
		return sDAO.selectAllFeedbackDTO();
	}

	@Override
	public List<FeedbackDTO> searchTotalFeedback() {
		return sDAO.selectTotalFeedbackDTO();
	}

	@Override
	public List<FeedbackDTO> searchAllNewFeedback() {
		return sDAO.selectAllNewFeedbackDTO();
	}

	@Override
	public List<FeedbackDTO> searchAllNiceFeedback() {
		return sDAO.selectAllNiceFeedbackDTO();
	}

	// ---------------------------------------------------------------------

	@Override
	public NoticeDTO searchOneNotice(String id) {
		sDAO.increaseNoticeHits(id);
		return sDAO.selectOneNoticeById(id);
	}

	@Override
	public FaqDTO searchOneFaq(String id) {
		return sDAO.selectOneFaqById(id);
	}

	@Override
	public FeedbackDTO searchOneFeedback(String id) {
		return sDAO.selectOneFeedbackById(id);
	}

	// ---------------------------------------------------------------------

	public boolean editNoticeTitle(String noticeId, String newTitle) {

		if (newTitle == null || newTitle.isBlank()) {
			throw new IllegalArgumentException();
		} // end if

		// 도메인 상태전이 동작을 서비스에서 수행한다.
		NoticeDTO target = sDAO.selectOneNoticeById(noticeId);
		if (target == null) {
			return false;
		} // end if
		if (newTitle.length() > 80) {
			throw new IllegalArgumentException("[공지사항] 수정할 공지사항 제목이 80자 초과.");
		} // end if

		sDAO.updateNoticeTitle(noticeId, newTitle);

		return true;
	}

	public boolean editNoticeContent(String noticeId, String newContent) {

		if (newContent == null || newContent.isBlank()) {
			throw new IllegalArgumentException();
		} // end if

		// 도메인 상태전이 동작을 서비스에서 수행한다.
		NoticeDTO target = sDAO.selectOneNoticeById(noticeId);
		if (target == null) {
			return false;
		} // end if
		if (newContent.length() > 1300) {
			throw new IllegalArgumentException("[공지사항] 수정할 공지사항 내용이 1300자 초과.");
		} // end if

		sDAO.updateNoticeContent(noticeId, newContent);

		return true;
	}

	public boolean editNoticeFixFlag(String noticeId, String fixFlag) {

		if (fixFlag == null || fixFlag.isBlank()) {
			throw new IllegalArgumentException();
		} // end if

		// 도메인 상태전이 동작을 서비스에서 수행한다.
		NoticeDTO target = sDAO.selectOneNoticeById(noticeId);
		if (target == null) {
			return false;
		} // end if
		if (fixFlag.length() > 9) {
			throw new IllegalArgumentException("[공지사항] 공지사항 고정여부 지정 오류.");
		} // end if

		sDAO.updateNoticeFixFlag(noticeId, fixFlag);

		return true;
	}

	@Override
	public boolean editNoticeHit(String id) {
		return false;
	}

	@Override
	public int editNoticeStatusInactive(String id) {
		return sDAO.updateNoticeStatusInactive(id);
	}

	@Override
	public int editFaqStatusInactive(String id) {
		return sDAO.updateFaqStatusInactive(id);
	}

	@Override
	public int editNoticeStatusActive(String id) {
		return sDAO.updateNoticeStatusActive(id);
	}

	// ---------------------------------------------------------------------

	public boolean editFaqTitle(String faqId, String newTitle) {

		if (newTitle == null || newTitle.isBlank()) {
			throw new IllegalArgumentException();
		} // end if

		// 도메인 상태전이 동작을 서비스에서 수행한다.
		FaqDTO target = sDAO.selectOneFaqById(faqId);
		if (target == null) {
			return false;
		} // end if
		if (newTitle.length() > 80) {
			throw new IllegalArgumentException("[공지사항] 수정할 FAQ 제목이 80자 초과.");
		} // end if

		sDAO.updateFaqTitle(faqId, newTitle);

		return true;
	}

	public boolean editFaqContent(String faqId, String newContent) {

		if (newContent == null || newContent.isBlank()) {
			throw new IllegalArgumentException();
		} // end if

		// 도메인 상태전이 동작을 서비스에서 수행한다.
		FaqDTO target = sDAO.selectOneFaqById(faqId);
		if (target == null) {
			return false;
		} // end if
		if (newContent.length() > 1300) {
			throw new IllegalArgumentException("[공지사항] 수정할 FAQ 내용이 1300자 초과.");
		} // end if

		sDAO.updateFaqContent(faqId, newContent);

		return true;
	}

	public List<FaqDTO> searchFaqsByType(String faqId) {
		List<FaqDTO> faqList = sDAO.selectFaqsByType(faqId);
		return faqList;
	}

	public boolean editFaqHit(String faqId) {
		boolean flag = sDAO.increaseFaqHits(faqId);
		return flag;
	}

	public boolean editFaqtype(String faqId, String fixFlag) {

		return false;
	}

	@Override
	public boolean editFaqStatus(String id) {
		return false;
	}

	// ---------------------------------------------------------------------
	@Override
	public boolean editFeedbackStatus(String id) {
		return sDAO.updateFeedbackStatus(id);
	}

	@Override
	public boolean editFeedbackStep(String id) {
		return sDAO.updateFeedbackStep(id);
	}

	@Override
	public boolean editFeedbackRefuse(String id) {
		return sDAO.updateFeedbackStepRefuse(id);
	}

	@Override
	public boolean editFeedbackStepFinished(String id) {
		return sDAO.updateFeedbackStepFinished(id);
	}

	// ---------------------------------------------------------------------

	@Override
	public boolean respondToFaq(String noticeId, String responseType) {
		return false;
	}

	@Override
	public boolean saveMarkFaq(String userId, String faqId) {
		return false;
	}

	@Override
	public boolean saveUnfinishedFeedback(String userId, String feedbackStatus) {
		return false;
	}

	@Override
	public boolean notifyFeedbackProgress(String feedbackId, String userId) {
		return false;
	}

	@Override
	public boolean suggestBetterKeywords(String rawKeyword) {
		return false;
	}

	@Override
	public boolean switctSupportCenterMode(String mode) {
		return false;
	}

	// ---------------------------------------------------------------------

	@Override
	public List<String> findTitlesByKeyword(String keyword) {
		List<NoticeDTO> noticeList = sDAO.selectNoticeTitlesByKeyword(keyword);

		List<String> titleList = new ArrayList<>();
		for (NoticeDTO dto : noticeList) {
			String title = dto.getNotice_title();
			if (title != null && title.startsWith(keyword)) {
				titleList.add(title);
			}
		}

		System.out.println("[NoticeServiceImpl] " + titleList.size() + "건의 키워드 반환.");
		return titleList;
	}

	@Override
	public List<NoticeDTO> findNoticesByKeyword(String keyword) {
		return sDAO.selectNoticesByKeyword(keyword);
	}

	@Override
	public NoticeDTO findOneByTitle(String title) {
		return null;
	}

	@Override
	public List<FaqDTO> selectFaqByType(String typeId) {
		return sDAO.selectFaqByType(typeId);
	}

	public String getCurrentFaqType(String typeId) {
		switch (typeId) {
		case "3000":
			return "결제";
		case "3001":
			return "강좌";
		case "3002":
			return "학습";
		case "3003":
			return "계정";
		case "3004":
			return "기타";
		default:
			return "오류발생";
		}
	}

	@Override
	public List<FaqDTO> searchAllFaqDTO() {
		return sDAO.selectAllFaqDTO();
	}

	@Override
	public List<FaqtypeDTO> selectActiveFaqTypes() {
		return sDAO.selectActiveFaqTypes();
	}

	@Override
	public List<FaqDTO> searchTop3Faq() {
		return sDAO.selectTop3FaqDTO();
	}

	public List<FaqDTO> searchFaqByTypeId(String typeId) {
		return sDAO.selectFaqByTypeId(typeId); // DAO에서 JDBC로 구현
	}

	public boolean updateNotice(NoticeDTO notice) {
		int updated = sDAO.updateNoticeById(notice);
		return updated > 0;
	}

	public boolean updateFaq(FaqDTO faq) {
		int updated = sDAO.updateFaqById(faq);
		return updated > 0;
	}

	public List<FaqtypeDTO> searchAllFaqtype() {
		return sDAO.selectAllFaqtype();
	};

	public int searchMaxFaqHits() {
		return sDAO.selectMaxFaqHits();
	};

	public int searchMaxFeedbackSends() {
		return sDAO.selectMaxFeedbackSends();
	};

	public List<FaqDTO> findFaqsByKeyword(String keyword) {
		return sDAO.selectFaqsByKeyword(keyword);
	}

}
