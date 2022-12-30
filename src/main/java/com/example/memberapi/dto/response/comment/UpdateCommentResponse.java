package com.example.memberapi.dto.response.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateCommentResponse {
    private final String comment;
}
