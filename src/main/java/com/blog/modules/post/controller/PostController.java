package com.blog.modules.post.controller;

import com.blog.common.api.ApiResponse;
import com.blog.common.api.PageResponse;
import com.blog.modules.post.dto.PostCreateRequest;
import com.blog.modules.post.dto.PostResponse;
import com.blog.modules.post.service.PostService;
import com.blog.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ApiResponse<PostResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                            @Valid @RequestBody PostCreateRequest request) {
        return ApiResponse.success(postService.publish(principal.getUserId(), request));
    }

    @GetMapping("/posts")
    public ApiResponse<PageResponse<PostResponse>> list(@AuthenticationPrincipal UserPrincipal principal,
                                                        @RequestParam(name = "page", defaultValue = "1") long page,
                                                        @RequestParam(name = "size", defaultValue = "10") long size,
                                                        @RequestParam(name = "userId", required = false) Long userId) {
        Long viewer = principal == null ? null : principal.getUserId();
        return ApiResponse.success(postService.listFeed(viewer, userId, page, size));
    }

    @GetMapping("/posts/{postId}")
    public ApiResponse<PostResponse> detail(@PathVariable("postId") Long postId,
                                            @AuthenticationPrincipal UserPrincipal principal) {
        Long viewer = principal == null ? null : principal.getUserId();
        return ApiResponse.success(postService.findDetail(postId, viewer));
    }
}

