package com.blog.modules.post.dto;

import com.blog.modules.post.entity.PostVisibility;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostResponse {

    private Long id;
    private String content;
    private boolean markdown;
    private List<String> images;
    private PostVisibility visibility;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostUserSummary author;
    private boolean liked;

    @Data
    @Builder
    public static class PostUserSummary {
        private Long id;
        private String username;
        private String avatarUrl;
    }
}

