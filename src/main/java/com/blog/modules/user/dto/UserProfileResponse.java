package com.blog.modules.user.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {

    private Long id;
    private String username;
    private String avatarUrl;
    private String bio;
    private LocalDateTime createdAt;
}

