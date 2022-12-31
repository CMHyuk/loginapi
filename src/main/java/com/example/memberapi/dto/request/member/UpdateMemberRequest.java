package com.example.memberapi.dto.request.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateMemberRequest {

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private String password;

}
