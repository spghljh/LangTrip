package kr.co.sist.e_learning.video;

import java.util.List;


public interface VideoService {
	
	void addVideo(VideoDTO cDTO);
	public List<VideoDTO> searchVideo();
	public VideoDTO showVideo(String videoSeq);
	public List<VideoDTO> searchVideoByCourseSeq(String courseSeq);
}
