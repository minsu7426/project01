package com.backend.project01.global.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

/**
 * 사용자 로그인 처리를 위해 AbstractAuthenticationProcessingFilter를 상속받은 객체
 * UsernamePasswordAuthenticationFilter를 참조하여 구현
 */
@Slf4j
public class JwtUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/v1/member/login";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String HTTP_METHOD = "POST";

    private static final AntPathRequestMatcher DEFAULT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL);

    public JwtUsernamePasswordAuthenticationFilter( ) {
        super(DEFAULT_PATH_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("JwtUsernamePasswordAuthenticationFilter 진입");
        return null;
    }
}
