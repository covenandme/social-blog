package com.blog.modules.comment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("comments")
public class Comment {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long postId;
    private Long parentId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
}

