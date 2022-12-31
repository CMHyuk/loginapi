package com.example.memberapi.service;

import com.example.memberapi.domain.Member;
import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.post.PostEdit;
import com.example.memberapi.dto.response.post.PostResponse;
import com.example.memberapi.repository.MemberRepository;
import com.example.memberapi.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PostServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 저장 테스트")
    void saveTest() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        //when
        Post savedPost = postService.save(post);

        //then
        assertEquals("제목", savedPost.getTitle());
        assertEquals("내용", savedPost.getContent());
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    void getTest() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        Post savedPost = postService.save(post);

        //when
        PostResponse postResponse = postService.get(savedPost.getId());

        //then
        assertEquals("제목", postResponse.getTitle());
        assertEquals("내용", postResponse.getContent());
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void editTest() {
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

        memberRepository.save(member);
        Post savedPost = postService.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목수정")
                .content("내용수정")
                .build();

        //when
        PostResponse postResponse = postService.edit(savedPost.getId(), postEdit, member);

        //then
        assertEquals("제목수정", postResponse.getTitle());
        assertEquals("내용수정", postResponse.getContent());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    @Transactional
    void deleteTest() {
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
        Post savedPost = postService.save(post);

        //when
        postService.delete(savedPost.getId(), member);

        //then
        assertEquals(0, postRepository.count());
    }
}