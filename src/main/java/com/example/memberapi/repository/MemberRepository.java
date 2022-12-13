package com.example.memberapi.repository;

import com.example.memberapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.loginId = :loginId")
    Optional<Member> findUser(@Param("loginId") String loginId);

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findById(Long id);
}
