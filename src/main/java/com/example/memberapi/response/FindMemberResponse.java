package com.example.memberapi.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FindMemberResponse<T> {
    private final T data;
}
