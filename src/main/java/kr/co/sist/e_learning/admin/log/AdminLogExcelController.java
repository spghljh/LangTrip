package kr.co.sist.e_learning.admin.log;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.e_learning.pagination.PageRequestDTO;
import kr.co.sist.e_learning.pagination.PageResponseDTO;

@Controller
public class AdminLogExcelController {

    @Autowired
    private AdminLogService adminLogService;

    @GetMapping("/admin/log/excel")
    public void downloadExcel(HttpServletResponse response,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String orderBy,
                              @RequestParam(required = false) String sort,
                              @RequestParam(required = false) String searchType,
                              @RequestParam(required = false) String searchKeyword,
                              @RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String endDate) throws IOException {

        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(page);
        pageRequestDTO.setSize(size);
        if (orderBy != null && !orderBy.isEmpty()) {
            pageRequestDTO.setOrderBy(orderBy);
        }
        if (sort != null && !sort.isEmpty()) {
            pageRequestDTO.setSort(sort);
        }
        
        AdminLogDTO searchDTO = new AdminLogDTO();
        searchDTO.setSearchType(searchType);
        searchDTO.setSearchKeyword(searchKeyword);
        searchDTO.setStartDate(startDate);
        searchDTO.setEndDate(endDate);

        PageResponseDTO<AdminLogDTO> pageResponse = adminLogService.getAdminLogs(pageRequestDTO, searchDTO);
        List<AdminLogDTO> logs = pageResponse.getList();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Admin Logs");

        // Header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Log ID", "Admin ID", "Action Type", "Target ID", "Log Time", "Details"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Data
        int rowNum = 1;
        for (AdminLogDTO log : logs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(log.getLogId());
            row.createCell(1).setCellValue(log.getAdminId());
            row.createCell(2).setCellValue(log.getActionType());
            row.createCell(3).setCellValue(log.getTargetId());
            row.createCell(4).setCellValue(log.getLogTime().toString());
            row.createCell(5).setCellValue(log.getDetails());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=admin_logs.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}