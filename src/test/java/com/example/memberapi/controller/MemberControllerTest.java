package com.example.memberapi.controller;

import com.example.memberapi.domain.Member;
import com.example.memberapi.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.memberapi.constant.SessionConst.LOGIN_MEMBER;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Mock
    private MockHttpSession mockHttpSession;

    @BeforeEach
    void clearRepository() {
        memberRepository.deleteAll();
    }

    @AfterEach
    void clearSession() {
        mockHttpSession.clearAttributes();
    }

    @BeforeEach
    void setUp() {
        mockHttpSession = new MockHttpSession();
    }

    @Test
    @DisplayName("/member/add 호출 시 회원 저장")
    void save() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        String json = objectMapper.writeValueAsString(member);

        //when
        mockMvc.perform(post("/member/add")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, memberRepository.count());
        Member findMember = memberRepository.findAll().get(0);
        assertEquals("아이디", findMember.getLoginId());
        assertEquals("비밀번호", findMember.getPassword());
    }

    @Test
    @DisplayName("/member/{id} 호출 시 회원 조회")
    void find() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();
        memberRepository.save(member);

        //expected
        mockMvc.perform(get("/member/{id}", member.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member.id").value(member.getId()))
                .andExpect(jsonPath("$.member.loginId").value("아이디"))
                .andExpect(jsonPath("$.member.password").value("비밀번호"))
                .andDo(print());
    }

    @Test
    @DisplayName("/member/all 호출 시 회원 전체 조회")
    void findMembers() throws Exception {
        //given
        List<Member> members = IntStream.range(0, 10)
                .mapToObj(i -> Member.builder()
                        .loginId("아이디" + i)
                        .password("비밀번호" + i)
                        .build())
                .collect(Collectors.toList());

        memberRepository.saveAll(members);

        //expected
        mockMvc.perform(get("/member/all")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].loginId").value("아이디0"))
                .andExpect(jsonPath("$[0].password").value("비밀번호0"))
                .andDo(print());
    }

    @Test
    @DisplayName("/member/update/{id} 호출 시 회원 수정")
    void updateMember() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();
        memberRepository.save(member);
        mockHttpSession.setAttribute(LOGIN_MEMBER, member);

        Member updateMember = Member.builder()
                .password("비밀번호변경")
                .build();

        String json = objectMapper.writeValueAsString(updateMember);

        //expected
        mockMvc.perform(patch("/member/update/{id}", member.getId())
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/member/update/{id} 미인증 401오류")
    @Transactional
    void createAuthErrorByUpdating() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();
        memberRepository.save(member);

        Member updateMember = Member.builder()
                .password("비밀번호변경")
                .build();

        String json = objectMapper.writeValueAsString(updateMember);

        //expected
        mockMvc.perform(patch("/member/update/{id}", member.getId())
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("/member/update/{id} 존재하지 않는 회원 수정")
    void updateNonExistMember() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();
        memberRepository.save(member);
        mockHttpSession.setAttribute(LOGIN_MEMBER, member);

        Member updateMember = Member.builder()
                .password("비밀번호변경")
                .build();

        String json = objectMapper.writeValueAsString(updateMember);

        //expected
        mockMvc.perform(patch("/member/update/{id}", 100L)
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("/member/delete/{id} 호출 시 회원 삭제")
    void deleteMember() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();
        memberRepository.save(member);
        mockHttpSession.setAttribute(LOGIN_MEMBER, member);

        //expected
        mockMvc.perform(delete("/member/delete/{id}", member.getId(), member)
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/member/delete/{id} 존재하지 않는 회원 삭제시 404오류")
    void deleteNonExistMember() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();
        memberRepository.save(member);
        mockHttpSession.setAttribute(LOGIN_MEMBER, member);

        //expected
        mockMvc.perform(delete("/member/delete/{id}", 100L, member)
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("/member/delete/{id} 미인증 401오류")
    @Transactional
    void createAuthErrorByDeleting() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        memberRepository.save(member);

        //expected
        mockMvc.perform(delete("/member/delete/{id}", member.getId(), member)
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("/member/{id} 존재하지 않는 회원 조회")
    void findNonExistMember() throws Exception {
        //expected
        mockMvc.perform(get("/member/{id}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}