package com.example.memberapi.dto.response.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FindMemberResponse {

    private final Long id;
    private final String loginId;
    private final String password;

    @Builder
    public FindMemberResponse(Long id, String loginId, String password) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
    }
}
