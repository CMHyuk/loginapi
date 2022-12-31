package com.example.memberapi.dto.response.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostDto {
    private final String title;
    private final String content;
}
