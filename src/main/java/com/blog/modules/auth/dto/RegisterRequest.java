package com.blog.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "用户名需为4-20位字母数字或下划线")
    private String username;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;
}

