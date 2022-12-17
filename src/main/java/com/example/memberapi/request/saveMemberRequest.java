package com.example.memberapi.request;

import lombok.Data;

@Data
public class saveMemberRequest {
    private String loginId;
    private String password;
}
