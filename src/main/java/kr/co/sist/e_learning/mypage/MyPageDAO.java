package kr.co.sist.e_learning.mypage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public interface MyPageDAO {
    MyPageDTO getUserInfo(long userSeq);  // 내정보 요약
    String selectProfilePath(long userSeq);  // 현재 경로 조회
    void updateProfile(@Param("userSeq") long userSeq, @Param("profile") String profile);
    List<SubscriptionDTO> selectSubscriptions(@Param("userSeq") Long userSeq);
    int deleteSubscription(Map<String, Object> paramMap);
}
