package com.example.memberapi.exception;

public class AuthException extends MemberException {

    private static final String MESSAGE = "미인증 사용자 요청입니다.";

    public AuthException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
