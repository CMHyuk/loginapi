package com.example.memberapi.controller;

import com.example.memberapi.domain.Member;
import com.example.memberapi.dto.request.reply.SaveReplyRequest;
import com.example.memberapi.dto.request.reply.UpdateReplyRequest;
import com.example.memberapi.dto.response.reply.UpdateReplyResponse;
import com.example.memberapi.service.ReplyService;
import com.example.memberapi.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/post/{postId}/{commentId}/reply")
    public void writeReply(@PathVariable Long postId, @PathVariable Long commentId,
                                          @RequestBody SaveReplyRequest request, @Login Member member) {
        replyService.saveReply(member.getLoginId(), postId, commentId, request);
    }

    @PatchMapping("/post/{postId}/comment/{commentId}/editReply/{replyId}")
    public UpdateReplyResponse editReply(@PathVariable Long postId, @PathVariable Long commentId,
                                         @PathVariable Long replyId, @RequestBody UpdateReplyRequest request,
                                         @Login Member member) {
        return replyService.updateReply(member.getLoginId(), postId, commentId, replyId, request);
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}/deleteReply/{replyId}")
    public void deleteReply(@PathVariable Long postId, @PathVariable Long commentId,
                            @PathVariable Long replyId, @Login Member member) {
        replyService.deleteReply(member.getLoginId(), postId, commentId, replyId);
    }
}
