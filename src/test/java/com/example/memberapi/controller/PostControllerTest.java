package com.example.memberapi.controller;

import com.example.memberapi.domain.Member;
import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.post.PostEdit;
import com.example.memberapi.repository.MemberRepository;
import com.example.memberapi.repository.PostRepository;
import com.example.memberapi.service.PostService;
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

import static com.example.memberapi.constant.SessionConst.LOGIN_MEMBER;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Mock
    private MockHttpSession mockHttpSession;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        mockHttpSession = new MockHttpSession();
    }

    @AfterEach
    void cleanSession() {
        mockHttpSession.clearAttributes();
    }

    @Test
    @DisplayName("/post/write 요청 시 게시글 저장")
    void saveTest() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        memberRepository.save(member);
        mockHttpSession.setAttribute(LOGIN_MEMBER, member);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        String json = objectMapper.writeValueAsString(post);

        //expected
        mockMvc.perform(post("/post/write")
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/write 요청 시 세션 없으면 오류")
    void createAuthErrorTest() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        memberRepository.save(member);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        String json = objectMapper.writeValueAsString(post);

        //expected
        mockMvc.perform(post("/post/write")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/{postId} 호출 시 게시글 조회")
    void getPostTest() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        memberRepository.save(member);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        postRepository.save(post);

        //expected
        mockMvc.perform(get("/post/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/{postId} 존재하지 않는 게시글 조회 시 오류")
    void getNonExistPostTest() throws Exception {
        //expected
        mockMvc.perform(get("/post/{postId}", 100L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/edit/{postId} 호출 시 게시글 수정")
    void editPostTest() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        memberRepository.save(member);
        mockHttpSession.setAttribute(LOGIN_MEMBER, member);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목수정")
                .content("내용수정")
                .build();

        String json = objectMapper.writeValueAsString(postEdit);

        //expected
        mockMvc.perform(patch("/post/edit/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/edit/{postId} 세션 없으면 오류")
    void editAuthErrorTest() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        memberRepository.save(member);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        postRepository.save(post);

        //expected
        mockMvc.perform(patch("/post/edit/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/edit/{postId} 존재하지 않는 게시글 수정 시 오류")
    void editNonExistPostTest() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        memberRepository.save(member);
        mockHttpSession.setAttribute(LOGIN_MEMBER, member);

        PostEdit postEdit = PostEdit.builder()
                .title("제목수정")
                .content("내용수정")
                .build();

        String json = objectMapper.writeValueAsString(postEdit);

        //expected
        mockMvc.perform(patch("/post/edit/{postId}", 100L)
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}