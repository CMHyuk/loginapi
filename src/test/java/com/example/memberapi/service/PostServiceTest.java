package com.example.memberapi.service;

import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.post.PostEdit;
import com.example.memberapi.dto.response.post.PostResponse;
import com.example.memberapi.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PostServiceTest {

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
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        Post savedPost = postService.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("제목수정")
                .content("내용수정")
                .build();

        //when
        Post editPost = postService.edit(savedPost.getId(), postEdit);

        //then
        assertEquals("제목수정", editPost.getTitle());
        assertEquals("내용수정", editPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deleteTest() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        Post savedPost = postService.save(post);

        //when
        postService.delete(savedPost.getId());

        //then
        assertEquals(0, postRepository.count());
    }
}