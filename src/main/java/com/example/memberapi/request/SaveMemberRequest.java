package com.example.memberapi.request;

import lombok.Data;

@Data
public class SaveMemberRequest {
    private String loginId;
    private String password;
}
