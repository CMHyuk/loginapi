package com.example.memberapi.exception;

public class MemberDuplication extends MemberException {

    private static final String MESSAGE = "이미 존재하는 회원입니다.";

    public MemberDuplication() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }
}
