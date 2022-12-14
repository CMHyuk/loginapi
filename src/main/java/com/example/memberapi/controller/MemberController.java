package com.example.memberapi.controller;

import com.example.memberapi.domain.Member;
import com.example.memberapi.dto.request.member.SaveMemberRequest;
import com.example.memberapi.dto.request.member.UpdateMemberRequest;
import com.example.memberapi.dto.response.member.CreateMemberResponse;
import com.example.memberapi.dto.response.member.FindMemberResponse;
import com.example.memberapi.dto.response.member.MemberDto;
import com.example.memberapi.dto.response.member.UpdateMemberResponse;
import com.example.memberapi.service.MemberService;
import com.example.memberapi.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/add")
    public CreateMemberResponse saveMember(@RequestBody @Valid SaveMemberRequest request) {
        Member savedMember = memberService.join(request);
        return new CreateMemberResponse(savedMember.getLoginId(), savedMember.getPassword());
    }

    @PatchMapping("/member/update/{id}")
    public UpdateMemberResponse updateMember(@PathVariable("id") Long id,
                                             @RequestBody @Valid UpdateMemberRequest request,
                                             HttpServletRequest httpServletRequest, @Login Member member) {
        UpdateMemberResponse memberResponse = memberService.update(id, request.getPassword(), member);
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return memberResponse;
    }

    @GetMapping("/member/all")
    public List<MemberDto> findMembers() {
        return memberService.findAll();
    }

    @GetMapping("/member/{id}")
    public FindMemberResponse findMember(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @DeleteMapping("/member/delete/{id}")
    public void deleteMember(@PathVariable Long id, @Login Member member) {
        memberService.delete(id, member);
    }
}
