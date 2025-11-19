package com.blog.modules.comment.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentResponse {

    private Long id;
    private Long postId;
    private Long parentId;
    private String content;
    private LocalDateTime createdAt;
    private AuthorSummary author;
    @Builder.Default
    private List<CommentResponse> replies = new ArrayList<>();

    @Data
    @Builder
    public static class AuthorSummary {
        private Long id;
        private String username;
        private String avatarUrl;
    }
}

