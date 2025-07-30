package kr.co.sist.e_learning.community.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDTO {
	
	 private int page;                 // 현재 페이지 번호
	    private int size;                 // 한 페이지당 게시글 수
	    private int totalPages;          // 총 페이지 수
	    private List<CommunityPostDTO> posts;
	    private String keyword;
}
