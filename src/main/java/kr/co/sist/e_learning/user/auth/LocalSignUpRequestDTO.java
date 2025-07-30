package kr.co.sist.e_learning.user.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalSignUpRequestDTO {
	private String nickname;
	private String password;
	private String email;
	private String signUp_path;
}
