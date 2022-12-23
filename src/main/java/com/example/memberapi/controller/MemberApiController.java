package com.example.memberapi.controller;

import com.example.memberapi.dto.MemberDto;
import com.example.memberapi.entity.Member;
import com.example.memberapi.request.UpdateMemberRequest;
import com.example.memberapi.request.SaveMemberRequest;
import com.example.memberapi.response.CreateMemberResponse;
import com.example.memberapi.response.UpdateMemberResponse;
import com.example.memberapi.response.FindMemberResponse;
import com.example.memberapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/member/add")
    public CreateMemberResponse saveMember(@RequestBody SaveMemberRequest request) {
        Member savedMember = memberService.join(request);
        return new CreateMemberResponse(savedMember.getLoginId(), savedMember.getPassword());
    }

    @PutMapping("/api/member/update/{id}")
    public UpdateMemberResponse updateMember(@PathVariable("id") Long id,
                                             @RequestBody @Validated UpdateMemberRequest request) {
        memberService.update(id, request.getPassword());
        Member findMember = memberService.findById(id).get();
        log.info("loginId = {}", findMember.getLoginId());
        log.info("password = {}", findMember.getPassword());
        return new UpdateMemberResponse(findMember.getLoginId(), findMember.getPassword());
    }

    @GetMapping("/api/member/find")
    public FindMemberResponse findMember() {
        List<Member> findMembers = memberService.findAll();
        List<MemberDto> memberList = findMembers.stream()
                .map(m -> new MemberDto(m.getLoginId()))
                .collect(Collectors.toList());

        return new FindMemberResponse(memberList);
    }

    @DeleteMapping("/api/member/delete/{id}")
    public void deleteMember(@PathVariable("id") Long id) {
        Member findMember = memberService.findById(id).get();
        memberService.delete(findMember);
    }
}
