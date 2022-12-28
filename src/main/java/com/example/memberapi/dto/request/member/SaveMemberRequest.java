package com.example.memberapi.dto.request.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SaveMemberRequest {

    @NotBlank(message = "아이디를 입력하세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

}
