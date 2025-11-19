package com.blog.modules.comment.controller;

import com.blog.common.api.ApiResponse;
import com.blog.modules.comment.dto.CommentRequest;
import com.blog.modules.comment.dto.CommentResponse;
import com.blog.modules.comment.service.CommentService;
import com.blog.security.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ApiResponse<List<CommentResponse>> list(@PathVariable("postId") Long postId) {
        return ApiResponse.success(commentService.listTreeByPost(postId));
    }

    @PostMapping
    public ApiResponse<CommentResponse> create(@PathVariable("postId") Long postId,
                                               @AuthenticationPrincipal UserPrincipal principal,
                                               @Valid @RequestBody CommentRequest request) {
        return ApiResponse.success(commentService.addComment(principal.getUserId(), postId, request));
    }
}

