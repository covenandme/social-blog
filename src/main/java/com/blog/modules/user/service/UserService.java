package com.blog.modules.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.modules.user.dto.UserProfileResponse;
import com.blog.modules.user.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends IService<User>, UserDetailsService {

    boolean existsByUsername(String username);

    UserProfileResponse convert(User user);
}

