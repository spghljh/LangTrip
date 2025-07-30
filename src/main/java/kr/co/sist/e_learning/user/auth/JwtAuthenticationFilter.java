package kr.co.sist.e_learning.user.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthUtils jwtAuthUtils;
    private final AuthService authService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, JwtAuthUtils jwtAuthUtils, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthUtils = jwtAuthUtils;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

       

        try {
            String accessToken = jwtAuthUtils.extractTokenFromCookies(request);

            if (accessToken != null) {
                
                if (jwtTokenProvider.validateToken(accessToken)) {
                    Long userSeq = jwtTokenProvider.getUserSeq(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userSeq, null));
                    
                } else {
                    
                    // 액세스 토큰이 유효하지 않으면 리프레시로 시도
                    try {
                        Long userSeq = authService.reissueAccessToken(request, response);
                        SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userSeq, null));
                        
                    } catch (Exception ex) {
                      
                        jwtAuthUtils.deleteAccessTokenCookie(response);
                        jwtAuthUtils.deleteRefreshTokenCookie(response);
                    }
                }
            } else {
                
            }
        } catch (ExpiredJwtException e) {
           
            jwtAuthUtils.deleteAccessTokenCookie(response);
        } catch (Exception e) {
          
            jwtAuthUtils.deleteAccessTokenCookie(response);
        }

        filterChain.doFilter(request, response);
    }
}
