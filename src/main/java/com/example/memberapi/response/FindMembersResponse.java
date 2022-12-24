package com.example.memberapi.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindMembersResponse<T> {
    private final T data;
}
