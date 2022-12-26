package com.example.memberapi.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateMemberResponse {

    private final String loginId;
    private final String password;

}
