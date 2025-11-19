package com.blog.modules.post.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.blog.common.model.BaseEntity;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "posts", autoResultMap = true)
public class Post extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String content;

    private Boolean markdown;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    private PostVisibility visibility;

    private Integer likeCount;

    private Integer commentCount;
}

