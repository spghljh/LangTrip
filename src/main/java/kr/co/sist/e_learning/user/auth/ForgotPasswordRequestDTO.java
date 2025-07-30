package kr.co.sist.e_learning.user.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequestDTO {
    private String nickname;
    private String email;
}
