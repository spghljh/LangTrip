package kr.co.sist.e_learning.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kr.co.sist.e_learning.community.dao.CommunityPostDAO;
import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityImageDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.community.dto.PageDTO;

@Service
public class CommunityPostServiceImpl implements CommunityPostService {

    @Autowired
    @Qualifier("communityPostDAOImpl")
    private CommunityPostDAO communityDAO;

    @Override
    public List<CommunityPostDTO> getAllPosts() {
        return communityDAO.selectAllPosts();
    }

    @Override
    public void writeRecommendation(CommunityPostDTO dto) {
        communityDAO.insertPost(dto);
    }

    @Override
    public CommunityPostDTO getRecommendation(Long postId) {
        return communityDAO.selectPost(postId);
    }

    @Override
    public void insertImage(CommunityImageDTO dto) {
        communityDAO.insertImage(dto);
    }

    @Override
    public void deletePost(Long postId) {
        communityDAO.deletePost(postId);
    }

    @Override
    public void writeCommet(CommunityCommentDTO cDTO) {
        communityDAO.insertComment(cDTO);
    }

    @Override
    public List<CommunityCommentDTO> getAllComments(Long postId) {
        return communityDAO.selectCommentList(postId);
    }

    @Override
    public List<CommunityPostDTO> getPostsPaginated(int offset, int limit) {
        return communityDAO.selectPostsPaginated(offset, limit);
    }

    @Override
    public int getTotalPostCount() {
        return communityDAO.selectTotalPostCount();
    }

    // ✅ 추가: 오늘 하루 개념글 리스트 (추천 많은 순)
    @Override
    public List<CommunityPostDTO> getBestPosts(int offset, int limit) {
        return communityDAO.selectBestPostsToday(offset, limit);
    }

    // ✅ (선택) 개념글 총 개수 - 페이징용으로 필요 시
    @Override
    public int getBestPostCount() {
        return communityDAO.countBestPostsToday();
    }
    
    @Override
    public void increaseViewCount(Long postId) {
    	communityDAO.increaseViewCount(postId);
    }
    
    @Override
    public List<CommunityPostDTO> getPostList(PageDTO pageDTO) {
        return communityDAO.selectPostList(pageDTO); // ✅ 올바른 호출
    }

    @Override
    public int getPostCount(PageDTO pageDTO) {
        return communityDAO.selectPostCount(pageDTO); // ✅ 올바른 호출
    }
    
    @Override
    public List<CommunityPostDTO> getPostsPaginatedWithSearch(int offset, int limit, String keyword) {
        return communityDAO.selectPostsPaginatedWithSearch(offset, limit, keyword);
    }

    @Override
    public int getTotalPostCountWithSearch(String keyword) {
        return communityDAO.selectTotalPostCountWithSearch(keyword);
    }

    
    
}
