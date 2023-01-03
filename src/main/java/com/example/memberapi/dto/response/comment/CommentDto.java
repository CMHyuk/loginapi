package com.example.memberapi.dto.response.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentDto {

    private final String comment;

    @Builder
    public CommentDto(String comment) {
        this.comment = comment;
    }
}
