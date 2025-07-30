package kr.co.sist.e_learning.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SupportController {

	private final SupportServiceImpl supportService;

	public SupportController(SupportServiceImpl supportService, NoticeDTO noticeDTO) {
		this.supportService = supportService;
	}

	// ---------------------------------------------------------------------------------------------

	// 지원센터 진입 From 사용자 : support_index.html
	@GetMapping("/support")
	public String index(Model model) {
		List<FaqDTO> topFaqs = supportService.searchTop3Faq();
		model.addAttribute("faqList", topFaqs);
		return "support/support_index";
	}

	@GetMapping("/faq/slider")
	public String getFaqSlider(Model model) {
		List<FaqDTO> topFaqs = supportService.searchTop3Faq();
		model.addAttribute("faqList", topFaqs);
		return "faq-slider";
	}

	// 전체 공지사항: notices.html
	@GetMapping("/support/notice")
	public String noticeForUser(@RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false) String keyword, Model model) {

		int pageSize = 10;
		List<NoticeDTO> noticeList = supportService.getPaginatedNotices(keyword, page, pageSize);
		int totalCount = supportService.getFilteredNoticeCount(keyword);
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);

		model.addAttribute("noticeList", noticeList);
		model.addAttribute("currPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("keyword", keyword);
		model.addAttribute("totalCount", totalCount);

		return "support/notice/notices";
	}

	// 상세 공지사항 : notice_detail.html
	@GetMapping("/support/notice/{id}")
	public String noticeDetailForUser(@PathVariable("id") String id, @RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false) String keyword, Model model) {

		NoticeDTO notice = supportService.searchOneNotice(id);

		if (notice == null) {
			model.addAttribute("errorMessage", "존재하지 않는 공지사항입니다.");
			return "support/notice/notice_error";
		}

		model.addAttribute("notice", notice);
		model.addAttribute("currPage", page);
		model.addAttribute("keyword", keyword);

		return "support/notice/notice_detail";
	}

	// FAQ 탐색 - 1 : faqs.html
	@GetMapping("/support/faq")
	public String faqUserStart(@RequestParam(required = false) String openId, Model model) {

		List<FaqtypeDTO> faqtypeList = supportService.searchAllFaqtype();
		List<FaqDTO> allFaqList = supportService.searchAllFaq();
		model.addAttribute("faqList", allFaqList);

		Map<String, List<FaqDTO>> groupedFaqs = new LinkedHashMap<>();
		for (FaqDTO faq : allFaqList) {
			groupedFaqs.computeIfAbsent(faq.getFaqtype_name(), k -> new ArrayList<>()).add(faq);
		}

		model.addAttribute("faqtypeList", faqtypeList);
		model.addAttribute("groupedFaqs", groupedFaqs);
		model.addAttribute("openId", openId);
		return "support/faq/faqs";
	}

	// FAQ 탐색 - 2 : faqs.html
	@GetMapping("/support/faq/{typeId}")
	public String loadFaqByType(@PathVariable("typeId") String typeId, Model model) {
		List<FaqDTO> faqList;

		if ("all".equalsIgnoreCase(typeId)) {
			faqList = supportService.searchAllFaq();
		} else {
			faqList = supportService.searchFaqByTypeId(typeId);
		}

		Map<String, List<FaqDTO>> groupedFaqs = new LinkedHashMap<>();
		for (FaqDTO faq : faqList) {
			groupedFaqs.computeIfAbsent(faq.getFaqtype_name(), k -> new ArrayList<>()).add(faq);
		}

		model.addAttribute("groupedFaqs", groupedFaqs);
		return "support/faq/faq-item :: faqItemFragment";
	}

	// FAQ 조회수 증가
	@PostMapping("/faq/hit")
	@ResponseBody
	public boolean increaseFaqHits(@RequestParam String id) {
		boolean success = supportService.editFaqHit(id);
		return success;
	}

	// ---------------------------------------------------------------------------------------------

	// 피드백 소개 : feedback_about.html
	@GetMapping("/support/feedback/about")
	public String feedbackUserStart() {
		return "support/feedback/feedback_about";
	}

	// 피드백 제출 - 1 : feedback_send.html
	@GetMapping("/support/feedback/send")
	public String feedbackSend(Model model) {

		Map<String, String> sessionUser = new HashMap<>();
		sessionUser.put("user_id", "paul"); // 샘플 로그인

//		model.addAttribute("sessionUser", sessionUser);
		model.addAttribute("sessionUser", null);

		List<FaqtypeDTO> faqtypeList = supportService.searchAllFaqtype();
		model.addAttribute("faqtypeList", faqtypeList);

		return "support/feedback/feedback_send";
	}

	// 피드백 제출 - 2
	@PostMapping("/support/feedback/submit")
	public String submitFeedback(@ModelAttribute FeedbackDTO feedbackDTO) {
		System.out.println("실명(1)/익명(0) 피드백 구분: " + feedbackDTO.getFeedback_style());
		supportService.addFeedback(feedbackDTO);
		return "redirect:/support/feedback/finished"; // 제출 완료 페이지
	}

	// 피드백 제출 - 3
	@GetMapping("/support/feedback/finished")
	public String feedbackFinished() {
		return "support/feedback/feedback_finished";
	}

	// ---------------------------------------------------------------------------------------------

	// 지원센터 진입 From 관리자 : support_admin.html
	@GetMapping("/admin/support")
	public String supportAdminStart(Model model) {

		List<NoticeDTO> noticeList = supportService.searchAllNoticeAdmin();
		model.addAttribute("noticeList", noticeList);
		return "support/admin/support_admin_notices";
	}

	// 공지사항 관리(전체조회) : support_admin_notices.html
	@GetMapping("/admin/support/noice")
	public String supportNotcieAdmin(Model model) {

		List<NoticeDTO> noticeList = supportService.searchAllNoticeAdmin();
		model.addAttribute("noticeList", noticeList);
		return "support/admin/support_admin_notices";
	}

	// 공지사항 관리(상세조회) : support_admin_notice_detail.html
	@GetMapping("/admin/support/notice/{id}")
	public String noticeDetailAdmin(@PathVariable("id") String id, Model model) {
		NoticeDTO notice = supportService.searchOneNotice(id);
		model.addAttribute("notice", notice);
		return "support/admin/support_admin_notice_detail";
	}

	// 공지사항 수정 페이지 : support_admin_notice_detail_edit2.html
	@GetMapping("/admin/support/notice/edit/{id}")
	public String editNoticeAdminPage(@PathVariable("id") String id, Model model) {
		NoticeDTO notice = supportService.searchOneNotice(id); // 해당 공지 조회
		model.addAttribute("notice", notice);
		return "support/admin/support_admin_notice_detail_edit";
	}

	// 공지사항 수정
	@PostMapping("/admin/support/notice/update/{id}")
	public String updateNotice(@PathVariable("id") String id, NoticeDTO notice, RedirectAttributes redirectAttributes) {
		notice.setNotice_id(id);

		boolean success = supportService.updateNotice(notice);
		if (success) {
			redirectAttributes.addFlashAttribute("message", "공지사항이 성공적으로 수정되었습니다.");
		} else {
			redirectAttributes.addFlashAttribute("error", "공지사항 수정에 실패했습니다.");
		}
		return "redirect:/admin/support/notice/" + id;
	}

	// 공지사항 비활성
	@PostMapping("/admin/support/notice/delete/{id}")
	public String deleteNoticeAdmin(@PathVariable("id") String id) {
		supportService.editNoticeStatusInactive(id);
		return "redirect:/admin/support";// 완료 후 관리자 공지사항 페이지로 복귀
	}

	// 공지사항 활성
	@PostMapping("/admin/supportnotice/active/{id}")
	public String activeNoticeAdmin(@PathVariable("id") String id) {
		supportService.editNoticeStatusActive(id);
		return "redirect:/admin/support";// 완료 후 관리자 공지사항 페이지로 복귀
	}

	// 공지사항 고정
	@PostMapping("/admin/support/notice/fix/{id}")
	public String fixNoticeAdmin(@PathVariable("id") String id) {

		boolean flag = supportService.editNoticeFixFlag(id, "Y");
		if (flag) {
			System.out.println("공지사항 공지 고정 완료 ");
		} else {
			System.out.println("에러발생 ");
		}

		return "redirect:/admin/support";// 완료 후 관리자 공지사항 페이지로 복귀
	}

	// 공지사항 고정해제
	@PostMapping("/admin/support/notice/unfix/{id}")
	public String unfixNoticeAdmin(@PathVariable("id") String id) {
		boolean flag = supportService.editNoticeFixFlag(id, "N");
		if (flag) {
			System.out.println("공지사항 공지 해제 완료 ");
		} else {
			System.out.println("에러발생 ");
		}
		return "redirect:/admin/support";// 완료 후 관리자 공지사항 페이지로 복귀
	}

	// FAQ 관리 : support_admin_faq.html
	@GetMapping("/admin/support/faq")
	public String supportFaqDetailAdmin(Model model) {

		// ---------------

		List<FaqDTO> graphData2 = supportService.searchAllFaqDTO();
		List<String> faqLabels = new ArrayList<>();
		List<Integer> faqHits = new ArrayList<>();

		for (FaqDTO dto : graphData2) {
			faqLabels.add(dto.getFaq_id());
			faqHits.add(dto.getFaq_hits()); // 조회수
		}
		model.addAttribute("faqChartLabels", faqLabels);
		model.addAttribute("faqChartValues", faqHits);

		// ---------------

		Map<String, Integer> type1Data = new LinkedHashMap<>();
		List<FaqDTO> faqList01 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList01) {
			if ("강좌".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type1Data.put(faqId, hits);
			}
		}
		model.addAttribute("type1Data", type1Data);

		Map<String, Integer> type2Data = new LinkedHashMap<>();
		List<FaqDTO> faqList02 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList02) {
			if ("학습".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type2Data.put(faqId, hits);
			}
		}
		model.addAttribute("type2Data", type2Data);

		Map<String, Integer> type3Data = new LinkedHashMap<>();
		List<FaqDTO> faqList03 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList03) {
			if ("결제".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type3Data.put(faqId, hits);
			}
		}
		model.addAttribute("type3Data", type3Data);

		Map<String, Integer> type4Data = new LinkedHashMap<>();
		List<FaqDTO> faqList04 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList04) {
			if ("계정".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type4Data.put(faqId, hits);
			}
		}
		model.addAttribute("type4Data", type4Data);

		Map<String, Integer> type5Data = new LinkedHashMap<>();
		List<FaqDTO> faqList05 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList05) {
			if ("기타".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type5Data.put(faqId, hits);
			}
		}
		model.addAttribute("type5Data", type5Data);

		Map<String, Integer> type6Data = new LinkedHashMap<>();
		List<FaqDTO> faqList06 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList06) {
			if ("기능".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type6Data.put(faqId, hits);
			}
		}
		model.addAttribute("type6Data", type6Data);

		// ---------------

		int maxHits = supportService.searchMaxFaqHits();
		model.addAttribute("maxHits", maxHits);

		List<FaqDTO> faqList = supportService.searchAllFaq();
		model.addAttribute("faqList", faqList);
		return "support/admin/support_admin_faq";
	}

	// FAQ 관리(상세조회)
//	@GetMapping("/admin/support/faq/{id}")
//	public String faqDetailAdmin(@PathVariable("id") String id, Model model) {
//		FaqDTO faq = supportService.searchOneFaq(id);
//		model.addAttribute("faq", faq);
//		return "support/admin/support_admin_faq_detail";
//	}

	// FAQ 수정 페이지 : support_admin_faq_detail_edit.html
	@GetMapping("/admin/support/faq/edit/{id}")
	public String editfaqAdminPage(@PathVariable("id") String id, Model model) {
		FaqDTO faq = supportService.searchOneFaq(id); // 해당 공지 조회
		model.addAttribute("faq", faq);
		return "support/admin/support_admin_faq_detail_edit";
	}

	// FAQ 추가 페이지 : support_admin_faq_detail_add.html
	@GetMapping("/admin/support/faq/add")
	public String addfaqAdminPage() {
		return "support/admin/support_admin_faq_detail_add";
	}

	// 공지사항 추가 페이지 : support_admin_faq_detail_add.html
	@GetMapping("/admin/support/notice/add")
	public String addNoticeAdminPage() {
		return "support/admin/support_admin_notice_detail_add";
	}

	// FAQ 수정
	@PostMapping("/admin/support/faq/update/{id}")
	public String updateFaq(@PathVariable("id") String id, FaqDTO faq, RedirectAttributes redirectAttributes) {
		faq.setFaq_id(id);

		boolean success = supportService.updateFaq(faq);
		if (success) {
			redirectAttributes.addFlashAttribute("message", "FAQ가 성공적으로 수정되었습니다.");
		} else {
			redirectAttributes.addFlashAttribute("error", "FAQ 수정에 실패했습니다.");
		}
		return "redirect:/admin/support/faq";
	}

	// FAQ 비활성
	@PostMapping("/admin/support/faq/delete/{id}")
	public String deleteFaqAdmin(@PathVariable("id") String id) {
		supportService.editFaqStatusInactive(id);
		return "redirect:/admin/support/faq";
	}

	// ---------------------------------------------------------------------------------------------

	// 관련 통계 : support_admin_total.html
	@GetMapping("/admin/support/total")
	public String supportAdminTotal(Model model) {

		Map<String, Integer> typeCountMap = new HashMap<>();
		List<FeedbackDTO> graphData = supportService.searchTotalFeedback();
		for (FeedbackDTO dto : graphData) {
			String type = dto.getFeedback_type(); // 예: "결제", "강좌"

			if (type.equals("3000")) {
				type = "결제";
			}
			if (type.equals("3001")) {
				type = "강좌";
			}
			if (type.equals("3002")) {
				type = "학습";
			}
			if (type.equals("3003")) {
				type = "계정";
			}
			if (type.equals("3004")) {
				type = "기타";
			}
			if (type.equals("3005")) {
				type = "기능";
			}

			typeCountMap.put(type, typeCountMap.getOrDefault(type, 0) + 1);
		}
		model.addAttribute("feedbackChartData", typeCountMap);

		int maxSends = supportService.searchMaxFeedbackSends();
		model.addAttribute("maxSends", maxSends);

		// ---------------

		List<FaqDTO> graphData2 = supportService.searchAllFaqDTO();
		List<String> faqLabels = new ArrayList<>();
		List<Integer> faqHits = new ArrayList<>();

		for (FaqDTO dto : graphData2) {
			faqLabels.add(dto.getFaq_id());
			faqHits.add(dto.getFaq_hits()); // 조회수
		}
		model.addAttribute("faqChartLabels", faqLabels);
		model.addAttribute("faqChartValues", faqHits);

		// ---------------

		Map<String, Integer> type1Data = new LinkedHashMap<>();
		List<FaqDTO> faqList01 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList01) {
			if ("강좌".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type1Data.put(faqId, hits);
			}
		}
		model.addAttribute("type1Data", type1Data);

		Map<String, Integer> type2Data = new LinkedHashMap<>();
		List<FaqDTO> faqList02 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList02) {
			if ("학습".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type2Data.put(faqId, hits);
			}
		}
		model.addAttribute("type2Data", type2Data);

		Map<String, Integer> type3Data = new LinkedHashMap<>();
		List<FaqDTO> faqList03 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList03) {
			if ("결제".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type3Data.put(faqId, hits);
			}
		}
		model.addAttribute("type3Data", type3Data);

		Map<String, Integer> type4Data = new LinkedHashMap<>();
		List<FaqDTO> faqList04 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList04) {
			if ("계정".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type4Data.put(faqId, hits);
			}
		}
		model.addAttribute("type4Data", type4Data);

		Map<String, Integer> type5Data = new LinkedHashMap<>();
		List<FaqDTO> faqList05 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList05) {
			if ("기타".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type5Data.put(faqId, hits);
			}
		}
		model.addAttribute("type5Data", type5Data);

		Map<String, Integer> type6Data = new LinkedHashMap<>();
		List<FaqDTO> faqList06 = supportService.searchAllFaqDTO();
		for (FaqDTO dto : faqList06) {
			if ("기능".equals(dto.getFaqtype_name())) {
				String faqId = dto.getFaq_title();
				int hits = dto.getFaq_hits();
				type6Data.put(faqId, hits);
			}
		}
		model.addAttribute("type6Data", type6Data);

		// ---------------

		int maxHits = supportService.searchMaxFaqHits();
		model.addAttribute("maxHits", maxHits);

		return "support/admin/support_admin_total";
	}

	// 신규/진행 피드백 조회 : support_admin_feedback.html
	@GetMapping("/admin/support/feedback")
	public String supportAdminFeedback(Model model) {

		Map<String, Integer> typeCountMap = new HashMap<>();
		List<FeedbackDTO> graphData = supportService.searchTotalFeedback();
		for (FeedbackDTO dto : graphData) {
			String type = dto.getFeedback_type(); // 예: "결제", "강좌"

			if (type.equals("3000")) {
				type = "결제";
			}
			if (type.equals("3001")) {
				type = "강좌";
			}
			if (type.equals("3002")) {
				type = "학습";
			}
			if (type.equals("3003")) {
				type = "계정";
			}
			if (type.equals("3004")) {
				type = "기타";
			}
			if (type.equals("3005")) {
				type = "기능";
			}

			typeCountMap.put(type, typeCountMap.getOrDefault(type, 0) + 1);
		}
		model.addAttribute("feedbackChartData", typeCountMap);

		int maxSends = supportService.searchMaxFeedbackSends();
		model.addAttribute("maxSends", maxSends);

		List<FeedbackDTO> newFeedbackList = supportService.searchAllNewFeedback();
		List<FeedbackDTO> niceFeedbackList = supportService.searchAllNiceFeedback();

		model.addAttribute("newFeedbackList", newFeedbackList);
		model.addAttribute("niceFeedbackList", niceFeedbackList);

		return "support/admin/support_admin_feedback";
	}

	// 전체 피드백 조회 : support_admin_feedbacks.html
	@GetMapping("/admin/support/feedbacks")
	public String supportAdminFeedbacks(Model model) {

		List<FeedbackDTO> feedbackList = supportService.searchAllFeedback();
		model.addAttribute("feedbackList", feedbackList);

		return "support/admin/support_admin_feedbacks";
	}

	@GetMapping("/admin/support/feedback_detail/{id}")
	public String getFeedbackDetailFragment(@PathVariable String id, Model model) {
		FeedbackDTO feedback = supportService.searchOneFeedback(id);
		model.addAttribute("feedback", feedback);
		return "support/admin/feedback_detail_fragment :: fragment";
	}

	// 피드백 접수(신규->진행)
	@PostMapping("/admin/support/feedback/stepUpdate")
	@ResponseBody
	public String updateStep(@RequestParam("id") String id) {
		boolean success = supportService.editFeedbackStep(id);
		return success ? "success" : "fail";
	}

	// 피드백 반려(신규->미반영)
	@PostMapping("/admin/support/feedback/refuse")
	@ResponseBody
	public String feedbackRefuse(@RequestParam("id") String id) {
		boolean success = supportService.editFeedbackRefuse(id);
		return success ? "success" : "fail";
	}

}// class
