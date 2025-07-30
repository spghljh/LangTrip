package kr.co.sist.e_learning.community.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VoteDAO {
    int hasVotedToday(@Param("userId") Integer userId, @Param("postId") Integer postId);
    void saveVote(@Param("userId") Integer userId, @Param("postId") Integer postId, @Param("voteType") String voteType);
    int countVotes(Map<String, Object> params);
}
