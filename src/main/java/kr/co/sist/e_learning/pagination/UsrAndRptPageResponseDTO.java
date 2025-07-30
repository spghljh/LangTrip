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
public class UsrAndRptPageResponseDTO<T> {
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_BLOCK_SIZE = 5;
	
	private List<T> list; // 결과 리스트
	private int totalCnt; // 전체 결과 수
	private int totalPages; // 전체 페이지 수
	private int currentPage; // 현재 페이지
	private int startPage; // 시작 페이지 번호
	private int endPage; // 끝 페이지 번호
	private boolean hasPrev; // 이전 블록 존재 여부
	private boolean hasNext; // 다음 블록 존재 여부
	
	// 기본값을 사용하는 생성자
	public UsrAndRptPageResponseDTO(List<T> list, int totalCnt, int currentPage) {
		this(list, totalCnt, currentPage, DEFAULT_PAGE_SIZE, DEFAULT_BLOCK_SIZE);
	}
	
	// 직접 지정 가능한 생성자
	public UsrAndRptPageResponseDTO(List<T> list, int totalCnt, int currentPage,
		int pageSize, int blockSize) {
		this.list = list;
		this.totalCnt = totalCnt;
		this.currentPage = currentPage;
		this.totalPages = (int)Math.ceil((double)totalCnt / pageSize);
		
	    if (this.totalPages == 0) {
	        this.totalPages = 1; // 최소 1페이지로 보정
	    }
		
		this.startPage = ((currentPage - 1) / blockSize) * blockSize + 1;
		this.endPage = Math.min(startPage + blockSize - 1, totalPages);
		
		this.hasPrev = startPage > 1;
		this.hasNext = endPage < totalPages;
	}
}
