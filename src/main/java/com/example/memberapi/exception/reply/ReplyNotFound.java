package com.example.memberapi.exception.reply;

import com.example.memberapi.exception.Exception;

public class ReplyNotFound extends Exception {

    private static final String MESSAGE = "존재하지 않는 대댓글입니다.";

    public ReplyNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
