package com.example.memberapi.service;

import com.example.memberapi.domain.Member;
import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.post.PostEdit;
import com.example.memberapi.dto.request.post.PostSearch;
import com.example.memberapi.dto.response.comment.CommentDto;
import com.example.memberapi.dto.response.post.PostResponse;
import com.example.memberapi.dto.response.post.PostSearchResponse;
import com.example.memberapi.exception.InvalidRequest;
import com.example.memberapi.exception.post.PostNotFound;
import com.example.memberapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .comments(post.getComments().stream()
                        .map(c -> new CommentDto(c.getComment()))
                        .collect(Collectors.toList()))
                .build();
    }

    public List<PostSearchResponse> findBySearch(String title) {
        List<Post> posts = postRepository.findByTitleContaining(title);
        return posts.stream()
                .map(p -> new PostSearchResponse(p.getId(), p.getTitle(), p.getMember().getLoginId()))
                .collect(Collectors.toList());
    }

    public List<PostSearchResponse> getAll(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(p -> new PostSearchResponse(p.getId(), p.getTitle(), p.getMember().getLoginId()))
                .collect(Collectors.toList());
    }

    public PostResponse edit(Long id, PostEdit postEdit, Member member) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        isWriter(member, post);

        post.setTitle(postEdit.getTitle());
        post.setContent(postEdit.getContent());

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .comments(post.getComments().stream()
                        .map(c -> new CommentDto(c.getComment()))
                        .collect(Collectors.toList()))
                .build();
    }

    public void delete(Long id, Member member) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        isWriter(member, post);
        postRepository.delete(post);
    }

    private void isWriter(Member member, Post post) {
        Long postMemberId = post.getMember().getId();
        Long loginMemberId = member.getId();

        if (!postMemberId.equals(loginMemberId)) {
            throw new InvalidRequest();
        }
    }
}
