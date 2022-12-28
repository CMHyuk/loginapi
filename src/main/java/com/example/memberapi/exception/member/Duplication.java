package com.example.memberapi.exception.member;

import com.example.memberapi.exception.Exception;

public class Duplication extends Exception {

    private static final String MESSAGE = "이미 존재하는 회원입니다.";

    public Duplication() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
