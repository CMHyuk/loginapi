package com.example.memberapi.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UpdateMemberResponse {
    private final String loginId;
    private final String password;
}
