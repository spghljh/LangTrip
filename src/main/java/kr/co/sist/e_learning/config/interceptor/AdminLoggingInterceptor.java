package kr.co.sist.e_learning.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.e_learning.admin.log.AdminLogDTO;
import kr.co.sist.e_learning.admin.log.AdminLogService;

@Component
public class AdminLoggingInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminLogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // Log only GET requests for page views
        if ("GET".equalsIgnoreCase(method)) {
            String adminId = getAdminId();
            String details = "IP: " + request.getRemoteAddr();

            AdminLogDTO logDTO = new AdminLogDTO();
            logDTO.setAdminId(adminId);
            logDTO.setActionType("PAGE_VIEW");
            logDTO.setTargetId(requestURI);
            logDTO.setDetails(details);

            logService.addLog(logDTO);
        }

        return true;
    }

    private String getAdminId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal != null) {
            return principal.toString();
        } else {
            return "anonymous";
        }
    }
}
