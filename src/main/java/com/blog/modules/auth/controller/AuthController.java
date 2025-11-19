package com.blog.modules.auth.controller;

import com.blog.common.api.ApiResponse;
import com.blog.common.exception.BizException;
import com.blog.common.exception.ErrorCode;
import com.blog.modules.auth.dto.AuthResponse;
import com.blog.modules.auth.dto.LoginRequest;
import com.blog.modules.auth.dto.RegisterRequest;
import com.blog.modules.user.dto.UserProfileResponse;
import com.blog.modules.user.entity.User;
import com.blog.modules.user.service.UserService;
import com.blog.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ApiResponse<UserProfileResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new BizException(ErrorCode.CONFLICT, "用户名已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAvatarUrl("https://api.dicebear.com/7.x/identicon/svg?seed=" + request.getUsername());
        user.setBio("这个人很懒，什么都没有写。");
        userService.save(user);
        return ApiResponse.success(userService.convert(user));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String token = tokenProvider.generateToken(authentication);
        User user = userService.lambdaQuery().eq(User::getUsername, request.getUsername()).one();
        return ApiResponse.success(AuthResponse.builder()
                .token(token)
                .profile(userService.convert(user))
                .build());
    }
}

