package com.example.memberapi.dto.request.reply;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateReplyRequest {

    @NotBlank(message = "대댓글을 입력하세요.")
    private String reply;
}
