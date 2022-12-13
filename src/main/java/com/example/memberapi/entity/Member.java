package com.example.memberapi.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String loginId;

    private String password;

    public Member(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
