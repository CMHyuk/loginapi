package com.example.memberapi.controller;

import com.example.memberapi.dto.MemberDto;
import com.example.memberapi.domain.Member;
import com.example.memberapi.dto.request.SaveMemberRequest;
import com.example.memberapi.dto.request.UpdateMemberRequest;
import com.example.memberapi.dto.response.CreateMemberResponse;
import com.example.memberapi.dto.response.FindMemberResponse;
import com.example.memberapi.dto.response.UpdateMemberResponse;
import com.example.memberapi.service.MemberService;
import com.example.memberapi.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/member/add")
    public CreateMemberResponse saveMember(@RequestBody SaveMemberRequest request) {
        Member savedMember = memberService.join(request);
        return new CreateMemberResponse(savedMember.getLoginId(), savedMember.getPassword());
    }

    @PatchMapping("/api/member/update/{id}")
    public UpdateMemberResponse updateMember(@PathVariable("id") Long id,
                                             @RequestBody @Validated UpdateMemberRequest request) {
        Member findMember = memberService.update(id, request.getPassword());
        return new UpdateMemberResponse(findMember.getLoginId(), findMember.getPassword());
    }

    @GetMapping("/api/member/all")
    public List<MemberDto> findMembers() {
        List<Member> findMembers = memberService.findAll();
        return findMembers.stream()
                .map(m -> new MemberDto(m.getLoginId(), m.getPassword()))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/member/{id}")
    public FindMemberResponse findMember(@PathVariable("id") Long id) {
        Member findMember = memberService.findById(id).get();
        return new FindMemberResponse(findMember);
    }

    @DeleteMapping("/api/member/delete/{id}")
    public void deleteMember(@PathVariable("id") Long id, @Login Member loginMember) {
        if (loginMember != null) {
            memberService.delete(id);
        }
    }
}
