package com.backend.project01.domain.member.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/")
@Slf4j
public class AdminApi {

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("/api/v1/admin/test 컨트롤러 진입");
        return ResponseEntity.ok().body("admin/test");
    }
}
