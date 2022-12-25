package com.example.memberapi.service;

import com.example.memberapi.entity.Member;
import com.example.memberapi.exception.MemberDuplication;
import com.example.memberapi.exception.MemberNotFound;
import com.example.memberapi.repository.MemberRepository;
import com.example.memberapi.request.SaveMemberRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository repository;

    @Autowired
    LoginService loginService;

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void successLogin() {
        //given
        SaveMemberRequest saveMember = new SaveMemberRequest();
        saveMember.setLoginId("login");
        saveMember.setPassword("password");

        memberService.join(saveMember);

        //when
        Member loginMember = loginService.login("login", "password");

        //then
        assertThat(loginMember).isNotNull();
    }

    @Test
    @DisplayName("로그인 실패")
    void failLogin() {
        //given
        SaveMemberRequest saveMember = new SaveMemberRequest();
        saveMember.setLoginId("login");
        saveMember.setPassword("password");

        memberService.join(saveMember);

        //expected
        assertThrows(MemberNotFound.class, () -> {
            loginService.login("login", "111");
        });
    }
}