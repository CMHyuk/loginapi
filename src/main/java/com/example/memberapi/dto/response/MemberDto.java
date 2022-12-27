package com.example.memberapi.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MemberDto {

    private final String loginId;
    private final String password;
}
