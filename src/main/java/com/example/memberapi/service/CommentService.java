package com.example.memberapi.service;

import com.example.memberapi.domain.Comment;
import com.example.memberapi.domain.Member;
import com.example.memberapi.domain.Post;
import com.example.memberapi.dto.request.comment.SaveCommentRequest;
import com.example.memberapi.dto.request.comment.UpdateCommentRequest;
import com.example.memberapi.dto.response.comment.SaveCommentResponse;
import com.example.memberapi.dto.response.comment.UpdateCommentResponse;
import com.example.memberapi.exception.InvalidRequest;
import com.example.memberapi.exception.comment.CommentNotFound;
import com.example.memberapi.exception.member.MemberNotFound;
import com.example.memberapi.exception.post.PostNotFound;
import com.example.memberapi.repository.CommentRepository;
import com.example.memberapi.repository.MemberRepository;
import com.example.memberapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public SaveCommentResponse write(String loginId, Long id, SaveCommentRequest saveCommentRequest) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(MemberNotFound::new);

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        Comment comment = Comment.builder()
                .comment(saveCommentRequest.getComment())
                .member(member)
                .post(post)
                .build();

        commentRepository.save(comment);

        return new SaveCommentResponse(comment.getComment());
    }

    public UpdateCommentResponse edit(String loginId, Long postId, Long commentId, UpdateCommentRequest request) {
        Comment comment = isCorrectRequest(loginId, postId, commentId);
        comment.setComment(request.getComment());
        return new UpdateCommentResponse(comment.getComment());
    }

    public void delete(String loginId, Long postId, Long commentId) {
        Comment comment = isCorrectRequest(loginId, postId, commentId);
        commentRepository.delete(comment);
    }

    private Comment isCorrectRequest(String loginId, Long postId, Long commentId) {
        Member findMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(MemberNotFound::new);

        postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        Long memberId = comment.getMember().getId();

        if (findMember.getId() != memberId) {
            throw new InvalidRequest();
        }
        return comment;
    }
}
