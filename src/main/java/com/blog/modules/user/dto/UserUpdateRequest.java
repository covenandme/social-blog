package com.blog.modules.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @Size(max = 255)
    private String avatarUrl;

    @Size(max = 255)
    private String bio;
}

