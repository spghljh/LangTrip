package kr.co.sist.e_learning.admin.auth;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminAuthDTO {
    private String adminId;
    private String adminPw;
    private String adminName;
    private String role; // This might be deprecated soon, using roles list instead
    private List<String> roles;
    private String status;

    // getters & setters
}
