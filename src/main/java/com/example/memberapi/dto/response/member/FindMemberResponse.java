package com.example.memberapi.dto.response.member;

import com.example.memberapi.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindMemberResponse {

    private final Member member;

}
