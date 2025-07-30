package kr.co.sist.e_learning.report;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
	private int reportId;
	private Date reportedAt;
	private String contentType;
	private String title;
	private int reporterId;
	private int reportedUserId;
	private String nickName;
	
	private List<Integer> reporterCheckedReason;
	private List<Integer> adminCheckedReason;
	private String reporterCustomReasonTxt;
	private String adminCustomReasonTxt;
	private Date adminActedAt;
	
	private String actionStatus;
	private String reportedUserStatus;
	
	private int postId2;
	private int courseId;
	
	public String getReporterReasons() {
		if (reporterCheckedReason == null || reporterCheckedReason.isEmpty()) {
			return "";
		}
		
		List<String> result = new ArrayList<String>();
		
		for (Integer code : reporterCheckedReason) {
			String reason = switch (code) {
				case 1 -> "욕설";
				case 2 -> "음란물";
				case 3 -> "허위";
				case 4 -> "기타";
				default -> "알 수 없음";
			};
			
			if (code == 4 && reporterCustomReasonTxt != null && !reporterCustomReasonTxt.isBlank()) {
			    result.add("기타(" + reporterCustomReasonTxt + ")");
			}
			
			if (code != 4) {
			    result.add(reason);
			}
		}
		
		return String.join(", ", result);
	}
	
	public String getReporterReasonsSimple() {
		if (reporterCheckedReason == null || reporterCheckedReason.isEmpty()) {
			return "";
		}
		
		List<String> result = new ArrayList<String>();
		for (Integer code : reporterCheckedReason) {
			String reason = switch (code) {
				case 1 -> "욕설";
				case 2 -> "음란물";
				case 3 -> "허위";
				case 4 -> "기타";
				default -> "알 수 없음";
			};
			result.add(reason);
		}
		return String.join(", ", result);
	}
	
	public String getAdminActedAtStr() {
		if (adminActedAt == null) {
			return "조치 없음";
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(adminActedAt);
	}
}
