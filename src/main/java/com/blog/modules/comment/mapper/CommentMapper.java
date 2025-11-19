package com.blog.modules.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.modules.comment.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}

