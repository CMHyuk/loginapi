package com.example.memberapi.dto.request.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class SaveCommentRequest {

    @NotBlank(message = "댓글을 입력하세요.")
    private String comment;

    @Builder
    public SaveCommentRequest(String comment) {
        this.comment = comment;
    }
}
