package com.backend.project01.domain.member.api;

import com.backend.project01.domain.member.application.MemberService;
import com.backend.project01.domain.member.dto.JoinRequest;
import com.backend.project01.global.jwt.MemberDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/member/")
@RequiredArgsConstructor
public class MemberApi {

    private final MemberService memberService;
    private final MemberDetailService memberDetailService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("/api/v1/member/test 컨트롤러 진입");
        return ResponseEntity.ok().body("member/test");
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinRequest joinRequest) {
        System.out.println("joinRequest = " + joinRequest);
        memberService.joinProcess(joinRequest);
        return ResponseEntity.ok().body("join OK");
    }
}
