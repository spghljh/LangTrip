package kr.co.sist.e_learning.community.dto;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor

public class CommunityPostDTO {

    private Long postId;
    private Long userId;
    private String title;
    private String content;
    private Timestamp createdAt;
    private int views;
    private char postState;
    private String nickname;
    private MultipartFile image;
    private int upCount;
    
    
    private String communityNotice;
    
}
