package com.example.memberapi.controller;

import com.example.memberapi.entity.Member;
import com.example.memberapi.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    public CreateMemberResponse saveMember(@RequestBody Member member) {
        Member savedMember = memberService.join(member);
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
    public Result findMember() {
        List<Member> findMembers = memberService.findAll();
        List<MemberDto> memberList = findMembers.stream()
                .map(m -> new MemberDto(m.getLoginId()))
                .collect(Collectors.toList());

        return new Result(memberList);
    }

    @DeleteMapping("/api/member/delete/{id}")
    public void deleteMember(@PathVariable("id") Long id) {
        Member findMember = memberService.findById(id).get();
        memberService.delete(findMember);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String loginId;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
    @Data
    static class CreateMemberResponse {
        private String loginId;
        private String password;

        public CreateMemberResponse(String loginId, String password) {
            this.loginId = loginId;
            this.password = password;
        }
    }

    @Data
    static class UpdateMemberResponse {
        private String loginId;
        private String password;

        public UpdateMemberResponse(String loginId, String password) {
            this.loginId = loginId;
            this.password = password;
        }
    }

    @Data
    static class UpdateMemberRequest {
        private String password;
    }
}
