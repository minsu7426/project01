package com.backend.project01.domain.member.repository;

import com.backend.project01.domain.member.domain.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {


}
