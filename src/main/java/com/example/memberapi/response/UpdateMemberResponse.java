package com.example.memberapi.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateMemberResponse {

    private final String loginId;
    private final String password;

}
