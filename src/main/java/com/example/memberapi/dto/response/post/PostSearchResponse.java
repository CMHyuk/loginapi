package com.example.memberapi.dto.response.post;

import com.example.memberapi.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostSearchResponse {

    private final List<Post> posts;

    @Builder
    public PostSearchResponse(List<Post> posts) {
        this.posts = posts;
    }
}
