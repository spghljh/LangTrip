package kr.co.sist.e_learning.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDTO {
    private Integer postId;
    private String voteType; // "UP" or "DOWN"
}
