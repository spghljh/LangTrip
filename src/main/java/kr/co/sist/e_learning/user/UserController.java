package kr.co.sist.e_learning.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/user")
public class UserController {
	
	@GetMapping("/user-list")
	public String userList() {
		
		return "admin/user/user-list";
	}
	
	// 어떤 컨트롤러 권장?
}
