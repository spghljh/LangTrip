package kr.co.sist.e_learning.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageDTO {

	//회원정보
    private long userSeq;
    private String email;
    
    private String password;
    public String getMaskedPassword() {
        if (password == null || password.length() <= 2) {
            return "***";
        }
        int maskLength = password.length() - 2;
        return password.substring(0, 2) + "*".repeat(maskLength);
    }
    private String nickname;
    private String status;
    private String profile;
    private String socialId;
    private String socialProvider;
    
    //통계자료
    private int completedCourses;
    private int totalStudyTime;
    private double quizAccuracy;
    private String donationLevel;
    private int totalDonation;
    
}
