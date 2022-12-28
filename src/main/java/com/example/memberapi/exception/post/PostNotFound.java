package com.example.memberapi.exception.post;

import com.example.memberapi.exception.Exception;

public class PostNotFound extends Exception {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
