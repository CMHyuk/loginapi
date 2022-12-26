package com.example.memberapi.controller;

import com.example.memberapi.domain.Member;
import com.example.memberapi.dto.request.LoginRequest;
import com.example.memberapi.dto.response.LoginResponse;
import com.example.memberapi.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.example.memberapi.constant.SessionConst.LOGIN_MEMBER;

@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final LoginService loginService;

    @PostMapping("/api/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Member loginMember = loginService.login(loginRequest.getLoginId(), loginRequest.getPassword());

        HttpSession session = request.getSession(false);
        session.setAttribute(LOGIN_MEMBER, loginMember);

        return new LoginResponse(loginMember.getLoginId(), loginRequest.getPassword());
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
