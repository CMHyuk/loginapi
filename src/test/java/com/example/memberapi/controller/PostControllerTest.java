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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.memberapi.constant.SessionConst.LOGIN_MEMBER;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        memberRepository.deleteAll();
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
    @DisplayName("/post/edit/{postId} 자신의 글이 아닌 다른 사람의 글 수정 시도시 오류")
    void editOtherPostTest() throws Exception {
        //given
        Member member1 = Member.builder()
                .loginId("1")
                .password("1")
                .build();

        Member member2 = Member.builder()
                .loginId("2")
                .password("2")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        mockHttpSession.setAttribute(LOGIN_MEMBER, member1);

        Post post1 = Post.builder()
                .title("제목1")
                .content("내용1")
                .member(member1)
                .build();

        postService.save(post1);

        Post post2 = Post.builder()
                .title("제목2")
                .content("내용2")
                .member(member2)
                .build();

        postService.save(post2);

        PostEdit postEdit = PostEdit.builder()
                .title("제목수정")
                .content("내용수정")
                .build();

        String json = objectMapper.writeValueAsString(postEdit);

        //expected
        mockMvc.perform(patch("/post/edit/{postId}", post2.getId())
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(json))
                .andExpect(status().isBadRequest())
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

    @Test
    @DisplayName("/post/delete/{postId} 호출 시 게시글 삭제")
    void deletePostTest() throws Exception {
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

        postService.save(post);

        //expected
        mockMvc.perform(delete("/post/delete/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/delete/{postId} 존재하지 않는 게시글 삭제 시 오류")
    void deleteNonExistPostTest() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        memberRepository.save(member);
        mockHttpSession.setAttribute(LOGIN_MEMBER, member);

        //expected
        mockMvc.perform(delete("/post/delete/{postId}", 100L)
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/delete/{postId} 세션 없으면 오류")
    void deleteAuthErrorTest() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postService.save(post);

        //expected
        mockMvc.perform(delete("/post/delete/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/delete/{postId} 다른 사람의 게시글 삭제 시 오류")
    void deleteOtherPostTest() throws Exception {
        //given
        Member member1 = Member.builder()
                .loginId("1")
                .password("1")
                .build();

        Member member2 = Member.builder()
                .loginId("2")
                .password("2")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        mockHttpSession.setAttribute(LOGIN_MEMBER, member1);

        Post post1 = Post.builder()
                .title("제목1")
                .content("내용1")
                .member(member1)
                .build();

        postService.save(post1);

        Post post2 = Post.builder()
                .title("제목2")
                .content("내용2")
                .member(member2)
                .build();

        postService.save(post2);

        //expected
        mockMvc.perform(delete("/post/delete/{postId}", post2.getId(), member1)
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/search 검색 조회")
    void searchPostTest() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        memberRepository.save(member);

        Post post1 = Post.builder()
                .title("1")
                .content("1")
                .member(member)
                .build();

        Post post2 = Post.builder()
                .title("111")
                .content("111")
                .member(member)
                .build();

        postRepository.save(post1);
        postRepository.save(post2);

        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
        info.add("title", "1");

        //expected
        mockMvc.perform(get("/post/search")
                        .contentType(APPLICATION_JSON)
                        .params(info))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].title").value("1"))
                .andExpect(jsonPath("$[0].loginId").value("아이디"))
                .andExpect(jsonPath("$[1].title").value("111"))
                .andExpect(jsonPath("$[1].loginId").value("아이디"))
                .andDo(print());
    }

    @Test
    @DisplayName("/post 페이징 테스트")
    void pagingTest() throws Exception {
        // given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        memberRepository.save(member);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("foo" + i)
                        .content("bar" + i)
                        .member(member)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/post?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("foo19"))
                .andDo(print());
    }
}