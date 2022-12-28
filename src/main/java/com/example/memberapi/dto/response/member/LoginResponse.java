package com.example.memberapi.dto.response.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {
    private final String loginId;
    private final String password;
}
