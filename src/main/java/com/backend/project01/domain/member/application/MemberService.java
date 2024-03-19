package com.backend.project01.domain.member.application;

import com.backend.project01.domain.member.domain.Member;
import com.backend.project01.domain.member.dto.JoinRequest;
import com.backend.project01.domain.member.error.DuplicateUsernameException;
import com.backend.project01.domain.member.repository.MemberRepository;
import com.backend.project01.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("id or password not valid"));

        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRoleType().toString())
                .build();
    }

    @Transactional
    public void joinProcess(JoinRequest joinRequest) {
        if(validateDuplicateMember(joinRequest.getUsername())) {
            throw new DuplicateUsernameException(ErrorCode.DUPLICATE_USERNAME);
        }
        Member member = joinRequest.toEntity();
        memberRepository.save(member.encode(passwordEncoder));
    }

    public boolean validateDuplicateMember(String username) {
        return memberRepository.existsByUsername(username);
    }

    @Transactional
    public void updateRefreshToken(String username, String refreshToken) {
        memberRepository.findByUsername(username).ifPresent(member -> member.updateRefreshToken(refreshToken));
    }


}
