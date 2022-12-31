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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/add")
    public CreateMemberResponse saveMember(@RequestBody SaveMemberRequest request) {
        Member savedMember = memberService.join(request);
        return new CreateMemberResponse(savedMember.getLoginId(), savedMember.getPassword());
    }

    @PatchMapping("/member/update/{id}")
    public UpdateMemberResponse updateMember(@PathVariable("id") Long id,
                                             @RequestBody @Validated UpdateMemberRequest request,
                                             HttpServletRequest httpServletRequest, @Login Member member) {
        Member findMember = memberService.update(id, request.getPassword(), member);
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new UpdateMemberResponse(findMember.getLoginId(), findMember.getPassword());
    }

    @GetMapping("/member/all")
    public List<MemberDto> findMembers() {
        List<Member> findMembers = memberService.findAll();
        return findMembers.stream()
                .map(m -> new MemberDto(m.getLoginId(), m.getPassword()))
                .collect(Collectors.toList());
    }

    @GetMapping("/member/{id}")
    public FindMemberResponse findMember(@PathVariable("id") Long id) {
        Member findMember = memberService.findById(id).get();
        return new FindMemberResponse(findMember);
    }

    @DeleteMapping("/member/delete/{id}")
    public void deleteMember(@PathVariable("id") Long id, @Login Member member) {
        memberService.delete(id, member);
    }
}
