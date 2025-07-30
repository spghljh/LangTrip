package kr.co.sist.e_learning.community.dto;

import java.sql.Timestamp;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityReportDTO {

	private Long reportId;
    private Long postId2;
    private Long reporterId;
    private String reasonText;
    private Timestamp reportedAt;
}
