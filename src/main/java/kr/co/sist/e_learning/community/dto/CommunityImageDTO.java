package kr.co.sist.e_learning.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityImageDTO {
	 private Long imageId;
	    private Long postId;
	    private Integer userId;
	    private String courseSeq;
	    private String imageUrl;
}
