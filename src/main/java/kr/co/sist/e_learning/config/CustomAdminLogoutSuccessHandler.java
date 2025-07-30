package kr.co.sist.e_learning.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.e_learning.admin.log.AdminLogDTO;
import kr.co.sist.e_learning.admin.log.AdminLogService;

@Component
public class CustomAdminLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private AdminLogService logService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String adminId = ((UserDetails) authentication.getPrincipal()).getUsername();
            String details = getRequestDetails();

            AdminLogDTO logDTO = new AdminLogDTO();
            logDTO.setAdminId(adminId);
            logDTO.setActionType("ADMIN_LOGOUT");
            logDTO.setTargetId(adminId);
            logDTO.setDetails(details);

            logService.addLog(logDTO);
        }

        response.sendRedirect("/admin/login");
    }

    private String getRequestDetails() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return "IP: " + request.getRemoteAddr();
        }
        return "";
    }
}
