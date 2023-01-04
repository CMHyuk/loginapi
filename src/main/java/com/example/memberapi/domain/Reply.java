package com.example.memberapi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Reply {

    @Id
    @GeneratedValue
    @Column(name = "REPLY_ID")
    private Long id;

    private String replyComment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

    @Builder
    public Reply(String replyComment, Member member, Post post, Comment comment) {
        this.replyComment = replyComment;
        this.member = member;
        this.post = post;
        this.comment = comment;
    }
}
