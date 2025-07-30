package kr.co.sist.e_learning.video;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class VideoController {

	@Value("${upload.saveDir}")
	private String saveDir;

	@Autowired
	private VideoService vs;

	@GetMapping("/upload/upload_video")
	public String showUploadForm(@RequestParam("seq") String courseSeq, Model model) {
		model.addAttribute("courseSeq", courseSeq);
		System.out.println(courseSeq);
		return "upload/upload_video";
	}
	
	
	
	@GetMapping("/ui/upload_frm")
	public String uploadFrm() {
		
		return "ui/upload_frm";
	}
	
	
	
	@PostMapping("/upload/upload_video")
	@ResponseBody
	public Map<String, Object> videoData(
			@RequestParam("upfile") List<MultipartFile> mfList,
			@ModelAttribute VideoDTO vDTO, Model model)
			throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		
		
		
		List<String> uploadedFiles = new ArrayList<>();
		List<VideoDTO> dtoList = new ArrayList<VideoDTO>();
		String errMsg = "";
		int videoCnt = 1;
		
		for (MultipartFile mf : mfList) {

			int maxSize = 1024 * 1024 * 200;

			if (mfList == null || mfList.isEmpty()) {
				errMsg = "올린 파일이 없습니다.";
				result.put("status", "fail");
				result.put("msg", errMsg);
				return result;
			}

			if (mf.getSize() > maxSize) {
				  result.put("status", "fail");
		          result.put("msg", "파일 크기는 200MB까지 가능합니다.");
		          return result;
			}

			if (!mf.getContentType().startsWith("video")) {
				result.put("status", "fail");
	            result.put("msg", "영상 파일만 업로드 가능합니다.");
	            return result;
			} // end if

			// 파일명 생성
			String originalName = mf.getOriginalFilename();
			String fileName = "";
			String fileExt = "";

			int fileSeperator = originalName.lastIndexOf(".");
			if (fileSeperator == -1) {
				fileSeperator = originalName.length();
			}

			fileName = originalName.substring(0, fileSeperator);
			if (originalName.contains(".")) {
				fileExt = originalName.substring(fileSeperator + 1);
			}
						
			String baseName= originalName.substring(0, fileSeperator);
			fileName = baseName;
			String fullName = fileName+"."+fileExt;
			String securityName = UUID.randomUUID().toString();
			
			String securityFileName = securityName +"."+fileExt;
			
			Path path = Paths.get(saveDir, securityFileName);
			
			//중복 1씩 증가 
			int cnt = 1;
			StringBuilder fileSB = new StringBuilder();
			while(Files.exists(path)) {
				 securityName = securityName + "(" + cnt + ")";
				 securityFileName = securityName + "." + fileExt;
				    path = Paths.get(saveDir, securityFileName);
				    cnt++;				
			   
			}

			// 디렉토리 생성
			Files.createDirectories(path.getParent());
			mf.transferTo(path.toFile());
			
			int wait = 0;
			while (!path.toFile().exists() && wait < 10) {
			    Thread.sleep(100); // 0.1초씩 대기
			    wait++;
			}
			System.out.println(fileSB.toString());
			
			
			
			VideoDTO newDTO = new VideoDTO();
			newDTO.setDescription(vDTO.getDescription());
			newDTO.setTitle(vDTO.getTitle());
			newDTO.setFileName(securityFileName);
			newDTO.setCourseSeq(vDTO.getCourseSeq());
			newDTO.setFilePath(path.toString());
			newDTO.setIsCompleted("N");
			newDTO.setUploadDate(new Date());
			newDTO.setVideoOrder(videoCnt);
			newDTO.setType("video");
			if(newDTO.getCourseSeq() != null && newDTO.getFileName() != null) {
				vs.addVideo(newDTO);
			}else {
				result.put("msg", "시발련아");
			}
			
			videoCnt++;
			
			dtoList.add(newDTO);
			result.put("cDTO", newDTO);
			
		} // end for
		
		result.put("status", "success");
		result.put("msg", "업로드 완료");
		result.put("videoList", dtoList);
		
		return result;
	}//upload
		
	@GetMapping("/video/watch_video")
	public String watchVideoList(Model model) {
		
		model.addAttribute("videoList", vs.searchVideo());
		
		return "video/watch_video";
	}
	
	@GetMapping("/video/watch")
	public String showVideo(@RequestParam String videoSeq, Model model) {
		VideoDTO vDTO = vs.showVideo(videoSeq);
		
		model.addAttribute("videoData", vDTO);
		System.out.println("파일이름 : "+vDTO.getFileName());
		System.out.println("파일제목 : "+vDTO.getTitle());
		System.out.println("파일설명 : "+vDTO.getDescription());
		System.out.println("파일이름 : "+vDTO.getFileName());
		
		return "video/video_frm";
	}
	
	

}
