package com.example.memberapi.controller;

import com.example.memberapi.domain.Member;
import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.post.PostCreate;
import com.example.memberapi.dto.request.post.PostEdit;
import com.example.memberapi.dto.request.post.PostSearch;
import com.example.memberapi.dto.response.post.PostResponse;
import com.example.memberapi.dto.response.post.PostSearchResponse;
import com.example.memberapi.service.PostService;
import com.example.memberapi.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/post")
    public List<PostSearchResponse> getPosts(@ModelAttribute PostSearch postSearch) {
        return postService.getAll(postSearch);
    }

    @GetMapping("/post/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/post/search")
    public List<PostSearchResponse> getPostSearch(@RequestParam String title) {
        return postService.findBySearch(title);
    }

    @PatchMapping("/post/edit/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit,
                             @Login Member member) {
        return postService.edit(postId, postEdit, member);
    }

    @DeleteMapping("/post/delete/{postId}")
    public void delete(@PathVariable Long postId, @Login Member member) {
        postService.delete(postId, member);
    }
}