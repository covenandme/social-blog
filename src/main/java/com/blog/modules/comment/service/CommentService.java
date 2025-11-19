package com.blog.modules.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.modules.comment.dto.CommentRequest;
import com.blog.modules.comment.dto.CommentResponse;
import com.blog.modules.comment.entity.Comment;
import java.util.List;

public interface CommentService extends IService<Comment> {

    CommentResponse addComment(Long userId, Long postId, CommentRequest request);

    List<CommentResponse> listTreeByPost(Long postId);
}

