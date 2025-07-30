package kr.co.sist.e_learning.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 정제균.
 * 
 * 페이지네이션을 위한 요청 DTO.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UsrAndRptPageRequestDTO {
	private Integer page = 1; // 요청 페이지 번호
	private Integer size = 10; // 한 페이지당 개수
	
	// 회원 관리용 검색 조건
	private String accountStatus;
	private String socialId;
	
	// 신고용 검색 조건
	private String reporterId;
	private String reportedUserId;
	private String contentType;
	private String actionStatus;
	private String sort; // 정렬 기준 (예: reportedAt,desc)
	
    public int getStartRow() {
        int p = (page != null) ? page : 1;
        int s = (size != null) ? size : 10;
        return (p - 1) * s + 1;
    }

    public int getEndRow() {
        int p = (page != null) ? page : 1;
        int s = (size != null) ? size : 10;
        return p * s;
    }
}
