package com.example.memberapi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String loginId;

    private String password;

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Member(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
