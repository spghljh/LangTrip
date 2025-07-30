package kr.co.sist.e_learning.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor; // Add this import
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
@AllArgsConstructor
@NoArgsConstructor // Add this annotation
public class PageRequestDTO {
	private int page = 1; // 요청 페이지 번호
	private int size = 10; // 한 페이지당 개수
	private String orderBy = "logTime"; // 정렬 기준 필드 (기본값 설정)
	private String sort = "desc"; // 정렬 방식 (asc, desc) (기본값 설정)
	
	// 회원 관리용 검색 조건
	private String accountStatus;
	private String socialId;
	
	// 신고용 검색 조건
	private String reporterId;
	private String reportedUserId;
	private String contentType;
	private String actionStatus;
	
	public int getOffset() {
		return (page - 1) * size;
	}

	public int getLimit() {
		return size;
	}

	public int getStartRow() {
		return (page - 1) * size + 1;
	}
	// 현재 페이지에서 조회할 마지막 행 번호
	public int getEndRow() {
		return page * size;
	}
}
