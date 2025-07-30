package kr.co.sist.e_learning.mypage;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LectureHistoryDAOImpl implements LectureHistoryDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<LectureHistoryDTO> getLectureHistory(long userSeq) {
        return sqlSession.selectList("kr.co.sist.e_learning.mypage.LectureHistoryMapper.selectLectureHistory", userSeq);
    }
    
    @Override
    public List<LectureHistoryDTO> selectMyLectures(long userSeq) {
        return sqlSession.selectList("kr.co.sist.e_learning.mypage.LectureHistoryMapper.selectMyLectures", userSeq);
    }

}
