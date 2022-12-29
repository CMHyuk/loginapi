package com.example.memberapi.service;

import com.example.memberapi.domain.Member;
import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.post.PostEdit;
import com.example.memberapi.dto.response.post.PostResponse;
import com.example.memberapi.exception.InvalidRequest;
import com.example.memberapi.exception.post.PostNotFound;
import com.example.memberapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .build();
    }

    public Post edit(Long id, PostEdit postEdit, Member member) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        isWriter(member, post);

        post.setTitle(postEdit.getTitle());
        post.setContent(postEdit.getContent());

        return post;
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
