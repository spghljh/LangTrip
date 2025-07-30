package kr.co.sist.e_learning.community.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunityCommentDTO {
	    private int commentId;
	    private Long postId2;
	    private Long userId2;
	    private String content;
	    private Integer parentId; 
	    private Timestamp createdAt;
	    private String cmtState;
	    private String nickname;
	    private int upCount;

	    @Override
	    public String toString() {
	        return "CommunityCommentDTO{" +
	                "commentId=" + commentId +
	                ", postId2=" + postId2 +
	                ", userId2='" + userId2 + '\'' +
	                ", content='" + content + '\'' +
	                ", parentId=" + parentId +
	                ", createdAt=" + createdAt +
	                ", cmtState='" + cmtState + '\'' +
	                ", nickname='" + nickname + '\'' +
	                '}';
	    }
}
