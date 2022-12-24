package com.example.memberapi.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateMemberRequest {

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private String password;

}
