package com.blog.modules.post.dto;

import com.blog.modules.post.entity.PostVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class PostCreateRequest {

    @NotBlank
    @Size(max = 10000)
    private String content;

    private boolean markdown;

    @Size(max = 9, message = "最多上传9张图片")
    private List<String> images;

    private PostVisibility visibility = PostVisibility.PUBLIC;
}

