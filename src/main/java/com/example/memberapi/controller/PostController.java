package com.example.memberapi.controller;

import com.example.memberapi.domain.Member;
import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.post.PostCreate;
import com.example.memberapi.dto.request.post.PostEdit;
import com.example.memberapi.dto.response.post.PostResponse;
import com.example.memberapi.service.PostService;
import com.example.memberapi.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post/write")
    public void post(@RequestBody @Valid PostCreate postCreate, @Login Member member) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .member(member)
                .build();
        postService.save(post);
    }

    @GetMapping("/post/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @PatchMapping("/post/edit/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
        Post editPost = postService.edit(postId, postEdit);
        return PostResponse.builder()
                .id(editPost.getId())
                .title(editPost.getTitle())
                .content(editPost.getContent())
                .build();
    }

    @DeleteMapping("/post/delete/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}