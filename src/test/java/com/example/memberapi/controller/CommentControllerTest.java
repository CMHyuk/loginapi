package com.example.memberapi.controller;

import com.example.memberapi.domain.Comment;
import com.example.memberapi.domain.Member;
import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.comment.SaveCommentRequest;
import com.example.memberapi.repository.CommentRepository;
import com.example.memberapi.repository.MemberRepository;
import com.example.memberapi.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.memberapi.constant.SessionConst.LOGIN_MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Mock
    private MockHttpSession mockHttpSession;

    @BeforeEach
    void clean() {
        commentRepository.deleteAll();
        memberRepository.deleteAll();
        postRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        mockHttpSession = new MockHttpSession();
    }

    @Test
    @DisplayName("post/{postId}/comment 댓글 저장")
    void saveCommentTest() throws Exception {
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

        Comment comment = Comment.builder()
                .comment("댓글")
                .member(member)
                .post(post)
                .build();

        String json = objectMapper.writeValueAsString(comment);

        //when
        mockMvc.perform(post("/post/{postId}/comment", post.getId())
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, commentRepository.count());
        List<Comment> comments = commentRepository.findAll();
        assertEquals("댓글", comments.get(0).getComment());
    }

    @Test
    @DisplayName("post/{postId}/comment 401 오류")
    void createAuthErrorCommentTest() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        Comment comment = Comment.builder()
                .comment("댓글")
                .post(post)
                .build();

        String json = objectMapper.writeValueAsString(comment);

        //expected
        mockMvc.perform(post("/post/{postId}/comment", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/{postId}/comment 404 오류")
    void createNotFoundErrorCommentTest() throws Exception {
        //given
        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        mockHttpSession.setAttribute(LOGIN_MEMBER, member);

        Comment comment = Comment.builder()
                .comment("댓글")
                .build();

        String json = objectMapper.writeValueAsString(comment);


        //expected
        mockMvc.perform(post("/post/{postId}/comment", 100L)
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("/post/{postId}/edit/comment 댓글 수정")
    void editCommentTest() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        Member member = Member.builder()
                .loginId("아이디")
                .password("비밀번호")
                .build();

        memberRepository.save(member);
        mockHttpSession.setAttribute(LOGIN_MEMBER, member);

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .comment("댓글")
                .build();

        commentRepository.save(comment);

        SaveCommentRequest saveCommentRequest = SaveCommentRequest.builder()
                .comment("댓글수정")
                .build();

        String json = objectMapper.writeValueAsString(saveCommentRequest);

        //when
        mockMvc.perform(patch("/post/{postId}/editComment/{commentId}",
                        post.getId(), comment.getId())
                        .contentType(APPLICATION_JSON)
                        .session(mockHttpSession)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }
}