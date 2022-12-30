package com.example.memberapi.controller;

import com.example.memberapi.domain.Member;
import com.example.memberapi.dto.request.comment.SaveCommentRequest;
import com.example.memberapi.dto.request.comment.UpdateCommentRequest;
import com.example.memberapi.dto.response.comment.SaveCommentResponse;
import com.example.memberapi.dto.response.comment.UpdateCommentResponse;
import com.example.memberapi.service.CommentService;
import com.example.memberapi.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    public SaveCommentResponse writeComment(@PathVariable Long postId, @Login Member member,
                                            @RequestBody SaveCommentRequest request) {
        return commentService.write(member.getLoginId(), postId, request);
    }

    @PatchMapping("/post/{postId}/editComment/{commentId}")
    public UpdateCommentResponse editComment(@PathVariable Long postId, @PathVariable Long commentId,
                                             @Login Member member, @RequestBody UpdateCommentRequest request) {
        return commentService.edit(member.getLoginId(), postId, commentId, request);
    }

    @DeleteMapping("/post/{postId}/deleteComment/{commentId}")
    public void deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                              @Login Member member) {
        commentService.delete(member.getLoginId(), postId, commentId);
    }
}
