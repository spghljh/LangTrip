package kr.co.sist.e_learning.chatgpt;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.core.net.server.Client;

@Service
public class OpenAiService {
	
	  @Value("${api_key}")
	  private String api_key;
	  @Value("${api_url}")
	  private String api_url;

	public String askChatGpt(String userMessage) throws Exception {

		HttpClient client = HttpClient.newHttpClient();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("role", "user");
		map.put("content", userMessage);

		Map<String, Object> requestBody = new HashMap<String, Object>();
		requestBody.put("model", "gpt-3.5-turbo");
		requestBody.put("messages", List.of(map));

		// java->json
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(requestBody);

		// openAI로 HTTP POST요청 만들기
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(api_url))
				.header("Content-Type", "application/json").header("Authorization", "Bearer " + api_key)
				.POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8)).build();

		
		//gpt로부터 응답(JSON) -> JAVA
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		Map<String, Object> result = mapper.readValue(response.body(), Map.class);

		if (result.containsKey("error")) {
		    Map<String, Object> error = (Map<String, Object>) result.get("error");
		    String errorMessage = error.get("message").toString();
		    return "GPT 응답 오류: " + errorMessage;
		}
		
		//choice에서 꺼내와서 첫번째 답변만 꺼낼 예정
		List<Map<String, Object>> choices = (List<Map<String, Object>>) result.get("choices");
		Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");

		return messageObj.get("content").toString().trim();
	}
}
