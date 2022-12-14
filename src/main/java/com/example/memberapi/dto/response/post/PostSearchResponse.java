package com.example.memberapi.dto.response.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchResponse {

    private final Long postId;
    private final String title;
    private final String loginId;

    @Builder
    public PostSearchResponse(Long postId, String title, String loginId) {
        this.postId = postId;
        this.title = title;
        this.loginId = loginId;
    }
}
