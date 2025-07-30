	package kr.co.sist.e_learning.community.dao;

import java.util.List;

import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityImageDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.community.dto.PageDTO;


public interface CommunityPostDAO {
    void insertPost(CommunityPostDTO dto);
    List<CommunityPostDTO> selectAllPosts();
    
    CommunityPostDTO selectPost(Long postId);
    void insertImage(CommunityImageDTO dto);

    void deletePost(Long postId); 
    
    int countAllPosts();
    
    List<CommunityCommentDTO> selectCommentList(Long postId);
    
    void insertComment(CommunityCommentDTO cDTO);
 
    List<CommunityPostDTO> selectPostsPaginated(int offset, int limit);

    int selectTotalPostCount();
    
  List<CommunityPostDTO> selectBestPostsToday(int offset, int limit);
    
    int countBestPostsToday();
    
    public void increaseViewCount(Long postId);
    
    public List<CommunityPostDTO> selectPostList(PageDTO pageDTO);
    public int selectPostCount(PageDTO pageDTO);
    
    List<CommunityPostDTO> selectPostsPaginatedWithSearch(int offset, int limit, String keyword);
    int selectTotalPostCountWithSearch(String keyword);

    
    
    
}
