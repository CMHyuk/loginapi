package com.example.memberapi.service;

import com.example.memberapi.domain.Comment;
import com.example.memberapi.domain.Member;
import com.example.memberapi.domain.Post;
import com.example.memberapi.domain.Reply;
import com.example.memberapi.dto.request.reply.SaveReplyRequest;
import com.example.memberapi.dto.request.reply.UpdateReplyRequest;
import com.example.memberapi.dto.response.reply.UpdateReplyResponse;
import com.example.memberapi.exception.InvalidRequest;
import com.example.memberapi.exception.comment.CommentNotFound;
import com.example.memberapi.exception.member.MemberNotFound;
import com.example.memberapi.exception.post.PostNotFound;
import com.example.memberapi.exception.reply.ReplyNotFound;
import com.example.memberapi.repository.CommentRepository;
import com.example.memberapi.repository.MemberRepository;
import com.example.memberapi.repository.PostRepository;
import com.example.memberapi.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void saveReply(String loginId, Long postId, Long commentId, SaveReplyRequest request) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(MemberNotFound::new);

        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        Reply reply = Reply.builder()
                .replyComment(request.getReply())
                .member(member)
                .post(post)
                .comment(comment)
                .build();

        replyRepository.save(reply);
    }

    public UpdateReplyResponse updateReply(String loginId, Long postId, Long commentId,
                                           Long replyId, UpdateReplyRequest request) {
        postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        Reply reply = isCorrectRequest(loginId, replyId);
        reply.setReplyComment(request.getReply());

        return UpdateReplyResponse.builder()
                .reply(reply.getReplyComment())
                .build();
    }

    public void deleteReply(String loginId, Long postId, Long commentId, Long replyId) {
        postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

        Reply reply = isCorrectRequest(loginId, replyId);
        replyRepository.delete(reply);
    }

    private Reply isCorrectRequest(String loginId, Long replyId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(MemberNotFound::new);

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(ReplyNotFound::new);

        if (member.getId() != reply.getMember().getId()) {
            throw new InvalidRequest();
        }
        return reply;
    }
}
