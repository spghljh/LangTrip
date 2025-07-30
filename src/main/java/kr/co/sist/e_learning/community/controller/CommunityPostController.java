package kr.co.sist.e_learning.community.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import kr.co.sist.e_learning.community.dto.PageDTO;
import kr.co.sist.e_learning.user.auth.UserAuthentication;
import kr.co.sist.e_learning.user.auth.UserEntity;
import kr.co.sist.e_learning.user.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.community.service.CommunityPostService;
import kr.co.sist.e_learning.community.service.VoteService;

import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/csj")
public class CommunityPostController {

    @Autowired
    private CommunityPostService communityService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir.root}")
    private String uploadDirRoot;

    @Value("${upload.path.community}")
    private String uploadPathWeb;

    private final String subFolder = "community";

    private Long getCurrentUserSeq() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }

    private UserEntity getCurrentUserEntity() {
        Long userSeq = getCurrentUserSeq();
        if (userSeq != null) {
            return userRepository.findByUserSeq(userSeq).orElse(null);
        }
        return null;
    }

    @GetMapping("/community")
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "50") int size,
                       @RequestParam(name = "tab", defaultValue = "all") String tab,
                       @RequestParam(name = "keyword", required = false) String keyword,
                       Model model) {

        int offset = (page - 1) * size;
        List<CommunityPostDTO> postList;
        int totalCount;

        if ("best".equals(tab)) {
            postList = communityService.getBestPosts(offset, size);
            totalCount = communityService.getBestPostCount();
            model.addAttribute("bestPostCount", totalCount);
        } else {
            postList = communityService.getPostsPaginatedWithSearch(offset, size, keyword);
            totalCount = communityService.getTotalPostCountWithSearch(keyword);
        }

        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("postList", postList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("tab", tab);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPostCount", totalCount);

        return "csj/community";
    }

    @GetMapping("/communityWrite")
    public String communityWrite(Model model) {
        UserEntity currentUser = getCurrentUserEntity();
        if (currentUser == null) {
            return null;
        }
        model.addAttribute("currentUserNickname", currentUser.getNickname());
        model.addAttribute("currentUserSeq", currentUser.getUserSeq());
        return "csj/communityWrite";
    }

    @PostMapping("/writeOk")
    public String writePost(CommunityPostDTO dto) {
        Long userSeq = getCurrentUserSeq();
        if (userSeq == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        dto.setUserId(userSeq);
        communityService.writeRecommendation(dto);
        return "redirect:/csj/community";
    }

    @GetMapping("/community/detail")
    public String detail(@RequestParam("postId") Long postId, Model model) {
        Long currentUserSeq = getCurrentUserSeq();
        communityService.increaseViewCount(postId);
        CommunityPostDTO post = communityService.getRecommendation(postId);
        List<CommunityCommentDTO> comments = communityService.getAllComments(postId);

        int upCount = voteService.getVoteCount(postId.intValue(), "UP");
        int downCount = voteService.getVoteCount(postId.intValue(), "DOWN");

        model.addAttribute("post", post);
        model.addAttribute("commentList", comments);
        model.addAttribute("upCount", upCount);
        model.addAttribute("downCount", downCount);
        model.addAttribute("currentUserSeq", currentUserSeq);
        return "csj/communityDetail";
    }

    @GetMapping("/comment/add")
    public String commentAdd(@RequestParam("postId") Long postId, Model model) {
        CommunityPostDTO post = communityService.getRecommendation(postId);
        model.addAttribute("post", post);
        return "csj/communityDetail";
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("image") MultipartFile imageFile) {
        if (getCurrentUserSeq() == null) {
            return "error: Not logged in";
        }

        try {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            File dir = new File(uploadDirRoot + "/community");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dest = new File(dir, fileName);
            imageFile.transferTo(dest);

            // 웹 접근용 경로 반환
            return uploadPathWeb + "/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/community/delete")
    public String deletePost(@RequestParam("postId") Long postId) {
        Long currentUserSeq = getCurrentUserSeq();
        if (currentUserSeq == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }

        CommunityPostDTO post = communityService.getRecommendation(postId);
        if (post != null && currentUserSeq.equals(post.getUserId())) {
            communityService.deletePost(postId);
        } else {
            throw new IllegalStateException("삭제 권한이 없거나 게시글을 찾을 수 없습니다.");
        }
        return "redirect:/csj/community";
    }

    @PostMapping("/comment/write")
    @ResponseBody
    public CommunityCommentDTO writeComment(@RequestBody CommunityCommentDTO commentDTO) {
        UserEntity currentUser = getCurrentUserEntity();
        if (currentUser == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }

        commentDTO.setUserId2(currentUser.getUserSeq());
        commentDTO.setNickname(currentUser.getNickname());

        if (commentDTO.getPostId2() == null || commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("postId2 또는 content가 비어 있음");
        }

        communityService.writeCommet(commentDTO);
        commentDTO.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return commentDTO;
    }

    @GetMapping("/csj/community")
    public String showCommunity(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "50") int size,
                                @RequestParam(required = false) String keyword,
                                Model model) {

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setSize(size);
        pageDTO.setKeyword(keyword);

        List<CommunityPostDTO> postList = communityService.getPostList(pageDTO);
        int totalCount = communityService.getPostCount(pageDTO);

        model.addAttribute("postList", postList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int)Math.ceil((double)totalCount / size));
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        return "csj/community";
    }
}
