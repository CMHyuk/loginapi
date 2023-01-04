package com.example.memberapi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "POST_ID")
    private Long id;

    private String title;

    @Lob
    private String content;

    private LocalDate localDate = LocalDate.now();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL)
    private List<Reply> replies = new ArrayList<>();

    @Builder
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }
}
