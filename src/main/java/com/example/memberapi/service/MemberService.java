package com.example.memberapi.service;

import com.example.memberapi.domain.Member;
import com.example.memberapi.dto.response.member.FindMemberResponse;
import com.example.memberapi.dto.response.member.MemberDto;
import com.example.memberapi.dto.response.member.UpdateMemberResponse;
import com.example.memberapi.exception.InvalidRequest;
import com.example.memberapi.exception.member.Duplication;
import com.example.memberapi.exception.member.MemberNotFound;
import com.example.memberapi.exception.member.PasswordDuplication;
import com.example.memberapi.repository.MemberRepository;
import com.example.memberapi.dto.request.member.SaveMemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(SaveMemberRequest saveMemberRequest) {
        Member member = Member.builder()
                .loginId(saveMemberRequest.getLoginId())
                .password(saveMemberRequest.getPassword())
                .build();

        validateDuplicatedMember(member);
        return memberRepository.save(member);
    }

    public Optional<Member> findByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(MemberNotFound::new);
        return Optional.ofNullable(member);
    }

    public FindMemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFound::new);

        return FindMemberResponse.builder()
                .id(member.getId())
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .build();
    }

    public UpdateMemberResponse update(Long id, String password, Member member) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(MemberNotFound::new);

        validateSameMember(member, findMember);
        validateDuplicatedPassword(password, member);
        findMember.setPassword(password);

        return UpdateMemberResponse.builder()
                .loginId(findMember.getLoginId())
                .password(findMember.getPassword())
                .build();
    }

    private void validateSameMember(Member member, Member findMember) {
        if (findMember.getId() != member.getId()) {
            throw new InvalidRequest();
        }
    }

    private void validateDuplicatedPassword(String password, Member member) {
        if (member.getPassword().equals(password)) {
            throw new PasswordDuplication();
        }
    }

    public List<MemberDto> findAll() {
        List<Member> findMembers = memberRepository.findAll();
        return findMembers.stream()
                .map(m -> new MemberDto(m.getLoginId(), m.getPassword()))
                .collect(Collectors.toList());
    }

    public void delete(Long id, Member member) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(MemberNotFound::new);
        validateSameMember(member, findMember);
        memberRepository.delete(member);
    }

    private void validateDuplicatedMember(Member member) {
        Optional<Member> user = memberRepository.findUser(member.getLoginId());
        if (!user.isEmpty()) {
            throw new Duplication();
        }
    }
}
