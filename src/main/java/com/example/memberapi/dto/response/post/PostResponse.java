package com.example.memberapi.dto.response.post;

import com.example.memberapi.domain.Comment;
import com.example.memberapi.dto.response.comment.CommentDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 서비스 정책에 맞는 클래스
 */
@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final List<CommentDto> comments;

    @Builder
    public PostResponse(Long id, String title, String content, List<CommentDto> comments) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
        this.comments = comments;
    }
}
