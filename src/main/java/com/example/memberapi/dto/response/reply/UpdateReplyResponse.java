package com.example.memberapi.dto.response.reply;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateReplyResponse {

    private final String reply;

    @Builder
    public UpdateReplyResponse(String reply) {
        this.reply = reply;
    }
}
