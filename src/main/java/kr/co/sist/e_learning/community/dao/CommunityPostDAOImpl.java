package kr.co.sist.e_learning.community.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityImageDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.community.dto.PageDTO;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

@Repository
@Primary
public class CommunityPostDAOImpl implements CommunityPostDAO {

    @Autowired
    private SqlSessionTemplate session;
    @Autowired
    private SqlSession sqlSession;

    private final String NS = "kr.co.sist.e_learning.community.dao.CommunityPostDAO";

    @Override
    public void insertPost(CommunityPostDTO dto) {
        session.insert(NS + ".insertPost", dto);
    }

    @Override
    public List<CommunityPostDTO> selectAllPosts() {
        return session.selectList(NS + ".selectAllPosts");
    }
    
    @Override
    public CommunityPostDTO selectPost(Long postId) {
        return session.selectOne(NS + ".selectPost", postId);
    }
    
    @Override
    public void insertImage(CommunityImageDTO dto) {
    	sqlSession.insert(NS + ".insertImage", dto);
    }
    
    @Override
    public void deletePost(Long postId) {
        session.delete(NS + ".deletePost", postId);
    }

	@Override
	public int countAllPosts() {
		 return sqlSession.selectOne(NS + ".countAllPosts");
	}

	
	
	@Override
	public void insertComment(CommunityCommentDTO cDTO) {
		sqlSession.insert(NS+".insertComment",cDTO);
	}
	
	@Override
	public List<CommunityCommentDTO> selectCommentList(Long postId) {
		
		return sqlSession.selectList(NS+".selectCommentList",postId);
	}

	@Override
	public List<CommunityPostDTO> selectPostsPaginated(int offset, int limit) {
	    Map<String, Integer> paramMap = new HashMap<>();
	    paramMap.put("offset", offset);
	    paramMap.put("limit", limit);
	    return session.selectList(NS + ".selectPostsPaginated", paramMap);
	}


	@Override
	public int selectTotalPostCount() {
	    return session.selectOne(NS + ".selectTotalPostCount");
	}
	 // ✅ 오늘 하루 추천 높은 게시글
    @Override
    public List<CommunityPostDTO> selectBestPostsToday(int offset, int limit) {
        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("limit", limit);
        return sqlSession.selectList(NS + ".selectBestPostsToday", param);
    }

    // ✅ 개념글 전체 수
    @Override
    public int countBestPostsToday() {
        return sqlSession.selectOne(NS + ".countBestPostsToday");
    }
    
    @Override
    public void increaseViewCount(Long postId) {
        sqlSession.update("kr.co.sist.e_learning.community.dao.CommunityPostDAO.increaseViewCount", postId);
    }
    
    
    @Override
    public List<CommunityPostDTO> selectPostList(PageDTO pageDTO) {
        return sqlSession.selectList("csjMapper.selectPostList", pageDTO);
    }

    @Override
    public int selectPostCount(PageDTO pageDTO) {
        return sqlSession.selectOne("csjMapper.selectPostCount", pageDTO);
    }

    @Override
    public List<CommunityPostDTO> selectPostsPaginatedWithSearch(int offset, int limit, String keyword) {
        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("limit", limit);
        param.put("keyword", keyword);
        return sqlSession.selectList(NS+".selectPostsPaginatedWithSearch", param);
    }

    @Override
    public int selectTotalPostCountWithSearch(String keyword) {
        return sqlSession.selectOne(NS+".selectTotalPostCountWithSearch", keyword);
    }

    
    
}

