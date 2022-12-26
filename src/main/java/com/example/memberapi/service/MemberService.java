package com.example.memberapi.service;

import com.example.memberapi.domain.Member;
import com.example.memberapi.exception.MemberDuplication;
import com.example.memberapi.exception.MemberNotFound;
import com.example.memberapi.exception.PasswordDuplication;
import com.example.memberapi.repository.MemberRepository;
import com.example.memberapi.dto.request.SaveMemberRequest;
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
        return memberRepository.findByLoginId(loginId);
    }

    public Optional<Member> findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFound::new);
        return Optional.ofNullable(member);
    }

    public Member update(Long id, String password) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFound::new);

        if (member.getPassword().equals(password)) {
            throw new PasswordDuplication();
        }

        member.setPassword(password);
        return member;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFound::new);
        memberRepository.delete(member);
    }

    private void validateDuplicatedMember(Member member) {
        Optional<Member> user = memberRepository.findUser(member.getLoginId());
        if (!user.isEmpty()) {
            throw new MemberDuplication();
        }
    }
}
