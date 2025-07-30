package kr.co.sist.e_learning.user;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long userSeq;
    private String email;
    private String password;
    private String nickname;
    private String status;
    private String profile;
    private String socialId;
    private String socialProvider;
    
    // 회원 관리. 정제균.
    // ui, uml 설계 보며 가져오기
    
    private Date createdAt;
    private Date adminActedAt; // 관리자 조치일. 없으면 조치 없음으로.
    private int openedCourseCnt;
    private int activeCourseCnt;
    private List<Integer> adminCheckedReason; // 이 파일에서 값 변환해야.
    private String adminCustomReasonTxt;
	private List<String> courseTitle;
	private List<String> communityTitle;
	// 개설한 과목 수와 수강 중인 과목 수는 쿼리를 만들어야 하나 자바 코드를 만들어야 하나
	
	// private String actionStatus; // 필요하나?
}
