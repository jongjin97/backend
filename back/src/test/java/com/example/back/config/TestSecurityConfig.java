package com.example.back.config;

import com.example.back.config.auth.OAuth2SuccessHandler;
import com.example.back.config.auth.PrincipalDetailService;
import com.example.back.config.auth.PrincipalOauth2UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(SecurityConfig.class)
@Configuration
public class TestSecurityConfig {
    @Bean
    public JwtProvider jwtProvider() {
        // JwtProvider의 가짜 구현체 또는 Mock을 반환
        return Mockito.mock(JwtProvider.class);
    }

    @Bean
    public PrincipalDetailService principalDetailService() {
        // PrincipalDetailService의 가짜 구현체 또는 Mock을 반환
        return Mockito.mock(PrincipalDetailService.class);
    }

    @Bean
    public PrincipalOauth2UserService principalOauth2UserService() {
        // PrincipalOauth2UserService의 가짜 구현체 또는 Mock을 반환
        return Mockito.mock(PrincipalOauth2UserService.class);
    }

    @Bean
    public OAuth2SuccessHandler successHandler() {
        // OAuth2SuccessHandler의 가짜 구현체 또는 Mock을 반환
        return Mockito.mock(OAuth2SuccessHandler.class);
    }
}
