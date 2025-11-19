package com.blog.modules.like.controller;

import com.blog.common.api.ApiResponse;
import com.blog.modules.like.service.PostLikeService;
import com.blog.modules.post.service.PostService;
import com.blog.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {

    private final PostLikeService postLikeService;
    private final PostService postService;

    public LikeController(PostLikeService postLikeService, PostService postService) {
        this.postLikeService = postLikeService;
        this.postService = postService;
    }

    @PostMapping
    public ApiResponse<Void> like(@PathVariable("postId") Long postId, @AuthenticationPrincipal UserPrincipal principal) {
        boolean created = postLikeService.like(principal.getUserId(), postId);
        if (created) {
            postService.increaseLike(postId, true);
        }
        return ApiResponse.success();
    }

    @DeleteMapping
    public ApiResponse<Void> cancel(@PathVariable("postId") Long postId, @AuthenticationPrincipal UserPrincipal principal) {
        boolean deleted = postLikeService.cancelLike(principal.getUserId(), postId);
        if (deleted) {
            postService.increaseLike(postId, false);
        }
        return ApiResponse.success();
    }
}

