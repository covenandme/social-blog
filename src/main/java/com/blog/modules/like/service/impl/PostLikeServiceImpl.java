package com.blog.modules.like.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.modules.like.entity.PostLike;
import com.blog.modules.like.mapper.PostLikeMapper;
import com.blog.modules.like.service.PostLikeService;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class PostLikeServiceImpl extends ServiceImpl<PostLikeMapper, PostLike> implements PostLikeService {

    @Override
    public boolean like(Long userId, Long postId) {
        boolean exists = lambdaQuery()
                .eq(PostLike::getUserId, userId)
                .eq(PostLike::getPostId, postId)
                .exists();
        if (exists) {
            return false;
        }
        PostLike like = new PostLike();
        like.setUserId(userId);
        like.setPostId(postId);
        return save(like);
    }

    @Override
    public boolean cancelLike(Long userId, Long postId) {
        return lambdaUpdate()
                .eq(PostLike::getUserId, userId)
                .eq(PostLike::getPostId, postId)
                .remove();
    }

    @Override
    public Set<Long> queryLikedPostIds(Long userId, Iterable<Long> postIds) {
        if (Objects.isNull(userId)) {
            return Set.of();
        }
        List<PostLike> likes = lambdaQuery()
                .eq(PostLike::getUserId, userId)
                .in(PostLike::getPostId, postIds)
                .list();
        Set<Long> ids = new HashSet<>();
        likes.forEach(like -> ids.add(like.getPostId()));
        return ids;
    }
}

