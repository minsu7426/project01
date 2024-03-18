package com.backend.project01.domain.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class JwtService {

    /*
     * JWT Token 암복호화시 사용할 키 값
     */
    private final String secret;

    /*
     * JWT Access Token 유효시간 (초)
     */
    private final long accessTokenValidityInSeconds;

    /*
     * JWT Refresh Token 유효시간 (초)
     */
    private final long refreshTokenValidityInSeconds;

    /*
     * HTTP Header에 Access Token을 포함시킬 때, Access Token임을 식별하기 위한 키 값
     */
    private final String accessHeader;

    /*
     * HTTP Header에 Refresh Token을 포함시킬 때, Refresh Token임을 식별하기 위한 키 값
     */
    private final String refreshHeader;

    private final Key key;

    private static final String USERNAME_CLAIM = "username";
    private static final String ROLES_CLAIM = "roles";
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access.expiration}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh.expiration}") long refreshTokenValidityInSeconds,
            @Value("${jwt.access.header}") String accessHeader,
            @Value("${jwt.refresh.header}") String refreshHeader) {
        this.secret = secret;
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds * 1000;
        this.accessHeader = accessHeader;
        this.refreshHeader = refreshHeader;
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String username, String roles) {
        log.info("새로운 Access Token 생성");

        Claims claims = Jwts.claims();
        claims.put(USERNAME_CLAIM, username);
        claims.put(ROLES_CLAIM, roles);

        System.out.println("토큰 만료 시간 = " + accessTokenValidityInSeconds);
        System.out.println("토큰 만료 일자 = " + new Date(System.currentTimeMillis() + accessTokenValidityInSeconds));

        return Jwts.builder()
//                .claim(USERNAME_CLAIM, username)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidityInSeconds))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken(String username, String roles) {
        log.info("새로운 Refresh Token 생성");

        Claims claims = Jwts.claims();
        claims.put(USERNAME_CLAIM, username);
        claims.put(ROLES_CLAIM, roles);

        System.out.println("토큰 만료 시간 = " + refreshTokenValidityInSeconds);
        System.out.println("토큰 만료 일자 = " + new Date(System.currentTimeMillis() + refreshTokenValidityInSeconds));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidityInSeconds))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(ACCESS_TOKEN_SUBJECT, BEARER + accessToken);
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get(USERNAME_CLAIM, String.class);
    }

    public String getRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get(ROLES_CLAIM, String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception ex) {
            System.out.println("token exception");
            return false;
        }
        return true;
    }
}
