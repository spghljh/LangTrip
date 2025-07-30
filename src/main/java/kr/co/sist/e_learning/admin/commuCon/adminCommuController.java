package kr.co.sist.e_learning.admin.commuCon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/adminDash")
public class adminCommuController {


    /**
     * 1) 전체 게시글 조회 (공지 포함)
     */
    @GetMapping("/admincommunity")
    public String listAllPosts() {
//        List<CommunityPostDTO> posts = commuService.findAllPosts();
//        model.addAttribute("posts", posts);
        return "adminDash/admincommunity";
    }
}
