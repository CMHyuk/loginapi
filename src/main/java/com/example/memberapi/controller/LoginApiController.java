package com.example.memberapi.controller;

import com.example.memberapi.entity.Member;
import com.example.memberapi.request.LoginRequest;
import com.example.memberapi.response.LoginResponse;
import com.example.memberapi.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final LoginService loginService;

    @PostMapping("/api/login")
    public LoginResponse loginController(@RequestBody LoginRequest loginRequest) {
        Member loginMember = loginService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        return new LoginResponse(loginMember.getLoginId(), loginRequest.getPassword());
    }
}
