package kr.co.sist.e_learning.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.e_learning.admin.log.AdminLogDTO;
import kr.co.sist.e_learning.admin.log.AdminLogService;

@Component
public class CustomAdminAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private AdminLogService logService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
        String adminId = request.getParameter("adminId");
        String details = getRequestDetails();

        AdminLogDTO logDTO = new AdminLogDTO();
        logDTO.setAdminId(adminId);
        logDTO.setActionType("ADMIN_LOGIN_FAILED");
        logDTO.setTargetId(adminId);
        logDTO.setDetails(details + ", Reason: " + exception.getMessage());

        logService.addLog(logDTO);

        response.sendRedirect("/admin/login?error=true");
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
