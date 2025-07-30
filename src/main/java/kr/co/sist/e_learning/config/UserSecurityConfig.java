package kr.co.sist.e_learning.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import kr.co.sist.e_learning.user.auth.AuthService;
import kr.co.sist.e_learning.user.auth.JwtAuthUtils;
import kr.co.sist.e_learning.user.auth.JwtAuthenticationFilter;
import kr.co.sist.e_learning.user.auth.JwtTokenProvider;

@Configuration
@Order(2)
public class UserSecurityConfig {

    @Autowired
    private CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtAuthUtils jwtAuthUtils;

    @Autowired
    private AuthService authService;


    @Bean
    public SecurityFilterChain userSecurity(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**") // admin 외 전부
            .authorizeHttpRequests(auth -> auth
            		 .requestMatchers(
            			        "/css/**", "/js/**", "/images/**", "/", 
            			        "/login", "/signup", "/social_signup",
            			        "/forgot-username", "/forgot-password", "/reset-password",
            			        "/user/logout", "/user/login/**",
            			        "/admin/**", // Permit all admin paths for UserSecurityConfig
            			        
            			        // 🔐 로그인 없이 접근 가능한 API 경로 추가
            			        "/api/auth/**"
            				 ).permitAll()
                .anyRequest().authenticated()
            )
      

            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .successHandler(customOAuth2AuthenticationSuccessHandler)
            )
            .addFilterBefore(jwtAuthenticationFilter(jwtTokenProvider, jwtAuthUtils, authService), UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable());

        return http.build();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtProvider,
                                                           JwtAuthUtils jwtAuthUtils,
                                                           AuthService authService) {
        return new JwtAuthenticationFilter(jwtProvider, jwtAuthUtils, authService);
    }

}
