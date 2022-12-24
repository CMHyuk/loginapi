package com.example.memberapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private String loginId;

    private String password;

    @Builder
    public Member(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
