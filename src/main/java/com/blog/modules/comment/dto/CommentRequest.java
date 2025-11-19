package com.blog.modules.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {

    private Long parentId;

    @NotBlank
    @Size(max = 1000)
    private String content;
}

