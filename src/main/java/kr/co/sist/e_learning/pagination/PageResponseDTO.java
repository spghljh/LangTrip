package kr.co.sist.e_learning.pagination;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 정제균.
 * 
 * 페이지네이션을 위한 응답 DTO.
 * @param <T>
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class PageResponseDTO<T> {
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_BLOCK_SIZE = 5;
	
	private List<T> list; // 결과 리스트
	private int totalCnt; // 전체 결과 수
	private int totalPages; // 전체 페이지 수
	private int page; // 현재 페이지
	private int startPage; // 시작 페이지 번호
	private int endPage; // 끝 페이지 번호
	private boolean hasPrev; // 이전 블록 존재 여부
	private boolean hasNext; // 다음 블록 존재 여부
	private int pageSize; // 페이지 크기 (추가)

	// 기본값을 사용하는 생성자
	public PageResponseDTO(List<T> list, int totalCnt, int page) {
		this(list, totalCnt, page, DEFAULT_PAGE_SIZE, DEFAULT_BLOCK_SIZE);
	}
	
	// 직접 지정 가능한 생성자
	public PageResponseDTO(List<T> list, int totalCnt, int page,
		int pageSize, int blockSize) {
		this.list = list;
		this.totalCnt = totalCnt;
		this.page = page;
		this.pageSize = pageSize; // 초기화
		this.totalPages = (totalCnt == 0) ? 1 : (int)Math.ceil((double)totalCnt / pageSize);
		
		this.startPage = ((page - 1) / blockSize) * blockSize + 1;
		// Ensure startPage is not greater than totalPages
		this.startPage = Math.min(this.startPage, this.totalPages);
		// Ensure startPage is at least 1
		this.startPage = Math.max(1, this.startPage);


		this.endPage = Math.min(startPage + blockSize - 1, totalPages);
		// Ensure endPage is not less than startPage
		this.endPage = Math.max(this.startPage, this.endPage);
		
		this.hasPrev = startPage > 1;
		this.hasNext = endPage < totalPages;
	}

	// startRow 계산 메서드 추가
	public int getStartRow() {
		return (page - 1) * pageSize + 1;
	}
}
