package com.example.memberapi.dto.response.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearchDto {
    private final Long postId;
    private final String title;

    @Builder
    public PostSearchDto(Long postId, String title) {
        this.postId = postId;
        this.title = title;
    }
}
