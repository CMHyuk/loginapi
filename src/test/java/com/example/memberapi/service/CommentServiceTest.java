package com.example.memberapi.service;

import com.example.memberapi.domain.Comment;
import com.example.memberapi.domain.Member;
import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.comment.SaveCommentRequest;
import com.example.memberapi.dto.request.comment.UpdateCommentRequest;
import com.example.memberapi.dto.response.comment.SaveCommentResponse;
import com.example.memberapi.repository.CommentRepository;
import com.example.memberapi.repository.MemberRepository;
import com.example.memberapi.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentService commentService;

    @BeforeEach
    void clean() {
        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성")
    void writeCommentTest() {
        //given
        Member member = Member.builder()
                .loginId("1")
                .password("1")
                .build();

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        Comment comment = Comment.builder()
                .comment("댓글")
                .member(member)
                .post(post)
                .build();

        memberRepository.save(member);
        postRepository.save(post);

        SaveCommentRequest saveCommentRequest = new SaveCommentRequest();
        saveCommentRequest.setComment(comment.getComment());

        //when
        SaveCommentResponse saveComment = commentService.write("1", post.getId(), saveCommentRequest);

        //then
        assertEquals("댓글", saveComment.getComment());
    }

    @Test
    @DisplayName("댓글 수정")
    void editCommentTest() {
        //given
        Member member = Member.builder()
                .loginId("1")
                .password("1")
                .build();

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        Comment comment = Comment.builder()
                .comment("댓글")
                .member(member)
                .post(post)
                .build();

        memberRepository.save(member);
        postRepository.save(post);
        commentRepository.save(comment);

        SaveCommentRequest saveCommentRequest = new SaveCommentRequest();
        saveCommentRequest.setComment(comment.getComment());

        commentService.write("1", post.getId(), saveCommentRequest);
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        updateCommentRequest.setComment("댓글수정");

        //when
        commentService.edit(member.getLoginId(), post.getId(), comment.getId(), updateCommentRequest);

        assertEquals("댓글수정", comment.getComment());
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteCommentTest() {
        //given
        Member member = Member.builder()
                .loginId("1")
                .password("1")
                .build();

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        Comment comment = Comment.builder()
                .comment("댓글")
                .member(member)
                .post(post)
                .build();

        memberRepository.save(member);
        postRepository.save(post);
        commentRepository.save(comment);

        SaveCommentRequest saveCommentRequest = new SaveCommentRequest();
        saveCommentRequest.setComment(comment.getComment());

        //when
        commentService.delete(member.getLoginId(), post.getId(), comment.getId());

        //then
        assertEquals(0, commentRepository.count());
    }
}