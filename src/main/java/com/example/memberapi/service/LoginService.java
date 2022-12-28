package com.example.memberapi.service;

import com.example.memberapi.domain.Member;
import com.example.memberapi.exception.member.MemberNotFound;
import com.example.memberapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElseThrow(MemberNotFound::new);
    }
}
