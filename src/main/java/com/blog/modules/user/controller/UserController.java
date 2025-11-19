package com.blog.modules.user.controller;

import com.blog.common.api.ApiResponse;
import com.blog.common.exception.BizException;
import com.blog.common.exception.ErrorCode;
import com.blog.modules.user.dto.UserProfileResponse;
import com.blog.modules.user.dto.UserUpdateRequest;
import com.blog.modules.user.entity.User;
import com.blog.modules.user.service.UserService;
import com.blog.security.UserPrincipal;
import jakarta.validation.Valid;
import java.util.Objects;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> current(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getById(principal.getUserId());
        return ApiResponse.success(userService.convert(user));
    }

    @PutMapping("/me")
    public ApiResponse<UserProfileResponse> update(@AuthenticationPrincipal UserPrincipal principal,
                                                   @Valid @RequestBody UserUpdateRequest request) {
        User user = userService.getById(principal.getUserId());
        if (Objects.nonNull(request.getAvatarUrl())) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (Objects.nonNull(request.getBio())) {
            user.setBio(request.getBio());
        }
        userService.updateById(user);
        return ApiResponse.success(userService.convert(user));
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> profile(@PathVariable("userId") Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        return ApiResponse.success(userService.convert(user));
    }
}

