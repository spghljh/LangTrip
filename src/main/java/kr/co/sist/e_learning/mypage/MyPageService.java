package kr.co.sist.e_learning.mypage;

import java.util.List;


public interface MyPageService {
    MyPageDTO getMyPageData(long userSeq);
    MyPageDTO getUserInfo(long userSeq);
    String selectProfilePath(long userSeq);
    void updateUserProfile(long userSeq, String newPath);
    List<LectureHistoryDTO> getLectureHistory(long userSeq);
    List<LectureHistoryDTO> selectMyLectures(long userSeq);
    List<SubscriptionDTO> getSubscriptions(Long userSeq);
    boolean cancelSubscription(Long userSeq, Long instructorId);
}

