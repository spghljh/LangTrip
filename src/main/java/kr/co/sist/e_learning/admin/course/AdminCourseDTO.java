package kr.co.sist.e_learning.admin.course;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class AdminCourseDTO {
    private String courseSeq;
    private String userSeq; // Creator's userSeq
    private String title;
    private String introduction;
    private String quizSeq;
    private LocalDateTime uploadDate;
    private String videoSeq;
    private String isPublic; // Y/N
    private String userName; // Creator's name
    private int videoCount;
    private int quizCount;
}