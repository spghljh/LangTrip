package kr.co.sist.e_learning.community.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.e_learning.community.dto.UsersssDTO;
import kr.co.sist.e_learning.community.dto.VoteDTO;
import kr.co.sist.e_learning.community.service.VoteService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/csj")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/vote")
    public ResponseEntity<?> vote(@RequestBody VoteDTO voteDTO, HttpSession session) {

        UsersssDTO loginUser = (UsersssDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        int userId = loginUser.getUserSeq();
        int postId = voteDTO.getPostId();
        String voteType = voteDTO.getVoteType();

        if (voteService.hasVotedToday(userId, postId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 오늘 투표하셨습니다.");
        }

        voteService.saveVote(userId, postId, voteType);

        // 투표 후 count 다시 조회해서 클라이언트에 내려줌
        int upVotes = voteService.getVoteCount(postId, "UP");
        int downVotes = voteService.getVoteCount(postId, "DOWN");

        return ResponseEntity.ok().body(Map.of("up", upVotes, "down", downVotes));
    }

}