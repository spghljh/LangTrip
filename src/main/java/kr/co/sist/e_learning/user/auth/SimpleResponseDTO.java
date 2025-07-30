package kr.co.sist.e_learning.user.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimpleResponseDTO {
    private boolean success;
    private String message;
    private Object data;
}
