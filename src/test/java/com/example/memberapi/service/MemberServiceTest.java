package com.example.memberapi.service;

import com.example.memberapi.domain.Member;
import com.example.memberapi.exception.member.Duplication;
import com.example.memberapi.exception.member.MemberNotFound;
import com.example.memberapi.exception.member.PasswordDuplication;
import com.example.memberapi.repository.MemberRepository;
import com.example.memberapi.dto.request.member.SaveMemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 저장 테스트")
    void saveTest() {
        //given
        SaveMemberRequest saveMemberRequest = new SaveMemberRequest();
        saveMemberRequest.setLoginId("아이디");
        saveMemberRequest.setPassword("비밀번호");

        //when
        Member joinMember = memberService.join(saveMemberRequest);

        //then
        assertEquals("아이디", joinMember.getLoginId());
        assertEquals("비밀번호", joinMember.getPassword());
    }

    @Test
    @DisplayName("회원 조회 테스트")
    void findTest() {
        //given
        List<Member> members = IntStream.range(0, 2)
                .mapToObj(i -> Member.builder()
                        .loginId("아이디" + i)
                        .password("비밀번호" + i)
                        .build())
                .collect(Collectors.toList());

        memberRepository.saveAll(members);

        //when
        List<Member> findMembers = memberService.findAll();

        //then
        assertEquals(2, findMembers.size());
        assertEquals("아이디0", findMembers.get(0).getLoginId());
        assertEquals("아이디1", findMembers.get(1).getLoginId());
        assertEquals("비밀번호0", findMembers.get(0).getPassword());
        assertEquals("비밀번호1", findMembers.get(1).getPassword());
    }

    @Test
    @DisplayName("회원 수정 테스트")
    void updateTest() {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        Member saveMember = memberRepository.save(member);

        //when
        Member updateMember = memberService.update(saveMember.getId(), "새로운비밀번호");

        //then
        assertEquals("새로운비밀번호", updateMember.getPassword());
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void deleteTest() {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        Member saveMember = memberRepository.save(member);

        //when
        memberService.delete(saveMember.getId());

        //then
        assertEquals(0, memberRepository.count());
    }

    @Test
    @DisplayName("중복 아이디 테스트")
    void checkDuplicatedLoginIdTest() {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        SaveMemberRequest saveMemberRequest = new SaveMemberRequest();
        saveMemberRequest.setLoginId("아이디");
        saveMemberRequest.setPassword("비밀번호");

        memberRepository.save(member);

        //expected
        assertThrows(Duplication.class, () -> {
            memberService.join(saveMemberRequest);
        });
    }

    @Test
    @DisplayName("비밀번호 변경 중복 테스트")
    void checkDuplicatedPasswordTest() {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        Member saveMember = memberRepository.save(member);

        //expected
        assertThrows(PasswordDuplication.class, () -> {
            memberService.update(saveMember.getId(), "비밀번호");
        });
    }

    @Test
    @DisplayName("존재하지 않는 회원 테스트")
    void findNonExistMemberTest() {
        assertThrows(MemberNotFound.class, () -> {
            memberService.findById(1L);
        });

        assertThrows(MemberNotFound.class, () -> {
            memberService.update(1L, "새로운비밀번호");
        });
    }
}