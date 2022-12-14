package com.example.memberapi.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Exception extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public Exception(String message) {
        super(message);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
