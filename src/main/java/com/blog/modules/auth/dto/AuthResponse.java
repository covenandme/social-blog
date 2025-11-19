package com.blog.modules.auth.dto;

import com.blog.modules.user.dto.UserProfileResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;
    private UserProfileResponse profile;
}

