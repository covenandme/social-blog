package com.blog.modules.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.modules.post.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
}

