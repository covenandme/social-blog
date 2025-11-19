package com.blog.modules.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.common.api.PageResponse;
import com.blog.modules.post.dto.PostCreateRequest;
import com.blog.modules.post.dto.PostResponse;
import com.blog.modules.post.entity.Post;

public interface PostService extends IService<Post> {

    PostResponse publish(Long userId, PostCreateRequest request);

    PageResponse<PostResponse> listFeed(Long viewerId, Long userId, long page, long size);

    void increaseLike(Long postId, boolean increment);

    void increaseComment(Long postId, boolean increment);

    PostResponse findDetail(Long postId, Long viewerId);
}

