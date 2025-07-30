package kr.co.sist.e_learning.report;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.sist.e_learning.pagination.UsrAndRptPageRequestDTO;

@Mapper
public interface ReportMapper {
	List<ReportDTO> selectReports(UsrAndRptPageRequestDTO pReqDTO);
	int countReports(UsrAndRptPageRequestDTO pReqDTO);
}
