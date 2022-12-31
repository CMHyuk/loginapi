package com.example.memberapi.service;

import com.example.memberapi.domain.Member;
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

    public Optional<Member> findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFound::new);
        return Optional.ofNullable(member);
    }

    public Member update(Long id, String password, Member member) {
        Member findMember = memberRepository.findById(id)
                .orElseThrow(MemberNotFound::new);

        validateSameMember(member, findMember);
        validateDuplicatedPassword(password, member);

        member.setPassword(password);
        return member;
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

    public List<Member> findAll() {
        return memberRepository.findAll();
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
