package kr.co.sist.e_learning.admin.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class CustomAdminDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomAdminDetailsService.class);

    @Autowired
    private AdminAuthDAO adminAuthDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminAuthDTO admin = adminAuthDAO.loginSelectAdminById(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Admin not found with id: " + username);
        }

        List<String> roles = adminAuthDAO.selectAdminRoles(admin.getAdminId());
        logger.info("Roles loaded for admin {}: {}", username, roles);
        admin.setRoles(roles);

        return new AdminUserDetails(admin);
    }
}
