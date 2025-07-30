package kr.co.sist.e_learning.admin.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AdminUserDetails implements UserDetails {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserDetails.class);

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String status;

    public AdminUserDetails(AdminAuthDTO admin) {
        this.username = admin.getAdminId();
        this.password = admin.getAdminPw();
        this.authorities = admin.getRoles().stream()
                .map(role -> {
                    logger.info("Processing role: {}", role);
                    return new SimpleGrantedAuthority(role);
                })
                .collect(Collectors.toList());
        logger.info("Authorities created for admin {}: {}", admin.getAdminId(), this.authorities);
        this.status = admin.getStatus();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "Y".equals(status);
    }
}
