package kr.co.sist.e_learning.donation;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LectureMapper {
    LectureDetailDTO selectLectureDetailByCourseSeq(@Param("courseSeq") String courseSeq);
}