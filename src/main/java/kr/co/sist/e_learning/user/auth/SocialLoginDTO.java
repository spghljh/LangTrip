package kr.co.sist.e_learning.user.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginDTO {
    private String nickname;
    private String socialId;
    private String socialProvider;
}

