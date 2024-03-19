package com.backend.project01.global.config.security;

import com.backend.project01.domain.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final List<String> NO_CHECK_URL_LIST = Arrays.asList(
            "/api/v1/member/join",
            "/api/v1/member/login"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthenticationFilter 진입");

        for (String url : NO_CHECK_URL_LIST) {
            if(request.getRequestURI().equals(url)) {
                log.info("no check url:{}", url);
                filterChain.doFilter(request, response);
                return;
            }
        }

        String accessToken = jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).orElse(null);

        if(accessToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");
            response.setHeader("isTokenValid", "false");
            return;
        }

        String username = jwtService.getUsername(accessToken);
        String roles = jwtService.getRole(accessToken);

        saveAuthentication(username, roles, accessToken);

        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(String username, String roles, String token) {

        List<SimpleGrantedAuthority> authorities = Arrays.stream(roles.split(","))
                .map(role -> role.replace("ROLE_", ""))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails userDetails = User.builder()
                .username(username)
                .password("empty")
                .roles(authorities.toString())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }


}
