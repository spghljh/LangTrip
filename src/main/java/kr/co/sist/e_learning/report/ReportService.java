package kr.co.sist.e_learning.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.e_learning.pagination.UsrAndRptPageRequestDTO;
import kr.co.sist.e_learning.pagination.UsrAndRptPageResponseDTO;

/**
 * 정제균.
 */
@Service
public class ReportService {
	private final ReportMapper reportMapper;
	
	// 생성자 주입
	@Autowired
	public ReportService(ReportMapper reportMapper) {
		this.reportMapper = reportMapper;
	}

	public UsrAndRptPageResponseDTO<ReportDTO> getReports(UsrAndRptPageRequestDTO pReqDTO) {
		List<ReportDTO> list = reportMapper.selectReports(pReqDTO);
		int totalCnt = reportMapper.countReports(pReqDTO);
		
		return new UsrAndRptPageResponseDTO<ReportDTO>(list, totalCnt, pReqDTO.getPage());
	}
}
