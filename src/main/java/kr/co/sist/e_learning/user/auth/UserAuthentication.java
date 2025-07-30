package kr.co.sist.e_learning.user.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthentication extends AbstractAuthenticationToken {

    private final Long userSeq;

    public UserAuthentication(Long userSeq, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userSeq = userSeq;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userSeq;
    }
}
