package com.blog.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.modules.user.dto.UserProfileResponse;
import com.blog.modules.user.entity.User;
import com.blog.modules.user.mapper.UserMapper;
import com.blog.modules.user.service.UserService;
import com.blog.security.UserPrincipal;
import java.util.Objects;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public boolean existsByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).exists();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = lambdaQuery().eq(User::getUsername, username).one();
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new UserPrincipal(user);
    }

    @Override
    public UserProfileResponse convert(User user) {
        if (user == null) {
            return null;
        }
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

