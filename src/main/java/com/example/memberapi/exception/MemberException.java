package com.example.memberapi.exception;

import lombok.Getter;

@Getter
public abstract class MemberException extends RuntimeException {

    public MemberException(String message) {
        super(message);
    }

    public abstract int getStatusCode();
}
