package kr.co.sist.e_learning.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.sist.e_learning.pagination.UsrAndRptPageRequestDTO;
import kr.co.sist.e_learning.pagination.UsrAndRptPageResponseDTO;

/**
 * 정제균.
 */
@Controller
@RequestMapping("/admin/report")
public class AdminReportController {
	private final ReportService reportService;
	
	// 생성자 주입
	@Autowired
	public AdminReportController(ReportService reportService) {
		this.reportService = reportService;
	}
	
	@GetMapping("/report-list")
	public String reportList(@ModelAttribute UsrAndRptPageRequestDTO pReqDTO, Model model) {
		UsrAndRptPageResponseDTO<ReportDTO> pResDTO = reportService.getReports(pReqDTO);
		model.addAttribute("pResDTO", pResDTO);
		model.addAttribute("pReqDTO", pReqDTO);
		return "admin/report/report-list";
	}
}
