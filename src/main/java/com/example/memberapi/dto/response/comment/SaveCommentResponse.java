package com.example.memberapi.dto.response.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SaveCommentResponse {
    private final String comment;
}
