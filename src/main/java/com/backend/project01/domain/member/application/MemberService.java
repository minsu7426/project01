package com.backend.project01.domain.member.application;

import com.backend.project01.domain.member.domain.Member;
import com.backend.project01.domain.member.dto.JoinRequest;
import com.backend.project01.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public void joinProcess(JoinRequest joinRequest) {
        Member member = joinRequest.toEntity();
        memberRepository.save(member.encode(passwordEncoder));
    }


}
