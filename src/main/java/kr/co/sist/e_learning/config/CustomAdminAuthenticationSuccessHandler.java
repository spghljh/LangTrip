package kr.co.sist.e_learning.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import kr.co.sist.e_learning.admin.log.AdminLogDTO;
import kr.co.sist.e_learning.admin.log.AdminLogService;

@Component
public class CustomAdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AdminLogService logService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        String adminId = authentication.getName();
        String details = getRequestDetails(request);

        AdminLogDTO logDTO = new AdminLogDTO();
        logDTO.setAdminId(adminId);
        logDTO.setActionType("ADMIN_LOGIN_SUCCESS");
        logDTO.setTargetId(adminId);
        logDTO.setDetails(details);

        logService.addLog(logDTO);

        HttpSession session = request.getSession();
        session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");

        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        
        String redirectUrl = "/admin/dashboard";

        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
            requestCache.removeRequest(request, response);
        }

        response.sendRedirect(redirectUrl);
    }

    private String getRequestDetails(HttpServletRequest request) {
        return "IP: " + request.getRemoteAddr();
    }
}
