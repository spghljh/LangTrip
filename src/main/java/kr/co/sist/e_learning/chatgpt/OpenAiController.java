package kr.co.sist.e_learning.chatgpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/chat")
public class OpenAiController {

	@GetMapping("/upload/useChatgpt")
	public String chatgpt() {
		
		return "upload/useChatgpt";
	}
	
	@Autowired
	private OpenAiService oas;
	
	@PostMapping
	@ResponseBody
	public String chat(@RequestBody String message) {
		try{
			return oas.askChatGpt(message);
		
		}catch(Exception e) {
			e.printStackTrace();
			return "오류발생";
		}
	}
}
