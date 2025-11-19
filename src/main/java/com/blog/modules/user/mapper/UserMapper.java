package com.blog.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.modules.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

