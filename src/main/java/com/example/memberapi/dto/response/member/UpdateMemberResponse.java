package com.example.memberapi.dto.response.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateMemberResponse {

    private final String loginId;
    private final String password;

    @Builder
    public UpdateMemberResponse(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
