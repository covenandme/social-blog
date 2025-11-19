package com.blog.modules.like.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.modules.like.entity.PostLike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostLikeMapper extends BaseMapper<PostLike> {
}

