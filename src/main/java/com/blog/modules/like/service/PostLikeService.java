package com.blog.modules.like.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.modules.like.entity.PostLike;
import java.util.Set;

public interface PostLikeService extends IService<PostLike> {

    boolean like(Long userId, Long postId);

    boolean cancelLike(Long userId, Long postId);

    Set<Long> queryLikedPostIds(Long userId, Iterable<Long> postIds);
}

