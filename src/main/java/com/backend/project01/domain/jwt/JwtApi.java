package com.backend.project01.domain.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jwt")
public class JwtApi {

    @PostMapping("/valid")
    public ResponseEntity<?> checkTokenValid(@RequestBody JwtRequest jwtRequest) {
        log.info("JwtRequest = {}", jwtRequest);

        String refreshToken = jwtRequest.getRefreshToken();

        return ResponseEntity.status(HttpStatus.OK).body("완료");
    }
}
