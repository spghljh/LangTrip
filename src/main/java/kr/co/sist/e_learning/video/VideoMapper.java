package kr.co.sist.e_learning.video;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface VideoMapper {
	
	void insertVideo(VideoDTO cDTO);
	
	public List<VideoDTO> selectVideo();
	
	public VideoDTO showVideo(String videoSeq);
	
	public List<VideoDTO>showVideoByCourseSeq(String courseSeq);
}
