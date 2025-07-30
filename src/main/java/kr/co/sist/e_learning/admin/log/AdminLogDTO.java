package kr.co.sist.e_learning.admin.log;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminLogDTO {
    private int logId;
    private String adminId;
    private String actionType;
    private String targetId;
    private Timestamp logTime;
    private String details;
    private String searchKeyword;
    private String searchType = "all";
    private String startDate;
    private String endDate;
}
