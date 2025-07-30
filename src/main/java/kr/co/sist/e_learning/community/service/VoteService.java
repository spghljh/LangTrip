package kr.co.sist.e_learning.community.service;

public interface VoteService {
    boolean hasVotedToday(Integer userId, Integer postId);
    void saveVote(Integer userId, Integer postId, String voteType);
    int getVoteCount(Integer postId, String voteType);
}