package com.backend.project01.domain.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class MemberEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", length = 20)
    private String userName;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "role_type")
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
