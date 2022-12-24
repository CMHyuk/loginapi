package com.example.memberapi.response;

import com.example.memberapi.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindMemberResponse {
    private final Member member;
}
