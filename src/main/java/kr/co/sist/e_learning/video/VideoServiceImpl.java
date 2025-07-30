package kr.co.sist.e_learning.video;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VideoServiceImpl implements VideoService{
	
	@Autowired
	private VideoMapper videoMapper;
	
	@Override
	public void addVideo(VideoDTO cDTO) {
		videoMapper.insertVideo(cDTO);
	}
	
	@Override
	public List<VideoDTO> searchVideo() {
		
		return videoMapper.selectVideo();
	}
	
	@Override
	public VideoDTO showVideo(String videoSeq) {
		return videoMapper.showVideo(videoSeq);
	}

	@Override
	public List<VideoDTO> searchVideoByCourseSeq(String courseSeq) {
		return videoMapper.showVideoByCourseSeq(courseSeq);
	}
	
	
	
	
}
