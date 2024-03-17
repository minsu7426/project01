package com.backend.project01.global.config;

import com.backend.project01.global.jwt.JwtUsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                //token을 사용하기 때문에 csrf disable
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                //JWT token을 사용하기 때문에 세션 정책을 stateless로 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/member/join", "/api/v1/member/login").permitAll()
                        .requestMatchers("/api/v1/member/test").permitAll()
                        .requestMatchers("/api/v1/admin/test").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .addFilterAfter(jwtUsernamePasswordAuthenticationFilter(), LogoutFilter.class)

                .getOrBuild();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter() {
        JwtUsernamePasswordAuthenticationFilter filter = new JwtUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
