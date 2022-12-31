package com.example.memberapi.controller;

import com.example.memberapi.domain.Member;
import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.post.PostCreate;
import com.example.memberapi.dto.request.post.PostEdit;
import com.example.memberapi.dto.response.post.PostDto;
import com.example.memberapi.dto.response.post.PostResponse;
import com.example.memberapi.service.PostService;
import com.example.memberapi.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<PostDto> getPosts() {
        List<Post> posts = postService.getAll();
        return posts.stream()
                .map(p -> new PostDto(p.getTitle(), p.getContent()))
                .collect(Collectors.toList());
    }

    @GetMapping("/post/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
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