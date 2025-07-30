package kr.co.sist.e_learning.video;


import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("VideoDTO")
public class VideoDTO {
    private int videoSeq;     // PK
    private String courseSeq;    // 강의 번호 (FK)
    private String title;
    private String filePath;     // 서버상의 실제 경로
    private String fileName;     // 원본 파일명
    private Date uploadDate;
    private String description;
    private String isCompleted;  // 완료 여부 (Y/N)
    private Integer videoOrder;  // 영상 순서
    private String type;
}