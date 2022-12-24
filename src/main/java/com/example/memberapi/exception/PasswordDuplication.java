package com.example.memberapi.exception;

public class PasswordDuplication extends MemberException {

    private static final String MESSAGE = "이전과 동일한 비밀번호입니다.";

    public PasswordDuplication() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
