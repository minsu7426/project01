package com.backend.project01.domain.member.dto;

import com.backend.project01.domain.member.domain.Member;
import com.backend.project01.domain.member.domain.RoleType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JoinRequest {

    private String username;

    private String password;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .roleType(RoleType.USER)
                .build();
    }
}
