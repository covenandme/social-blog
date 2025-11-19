package com.blog.modules.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.common.api.PageResponse;
import com.blog.common.exception.BizException;
import com.blog.common.exception.ErrorCode;
import com.blog.modules.like.service.PostLikeService;
import com.blog.modules.post.dto.PostCreateRequest;
import com.blog.modules.post.dto.PostResponse;
import com.blog.modules.post.entity.Post;
import com.blog.modules.post.entity.PostVisibility;
import com.blog.modules.post.mapper.PostMapper;
import com.blog.modules.post.service.PostService;
import com.blog.modules.user.entity.User;
import com.blog.modules.user.service.UserService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    private final UserService userService;
    private final PostLikeService postLikeService;

    public PostServiceImpl(UserService userService, PostLikeService postLikeService) {
        this.userService = userService;
        this.postLikeService = postLikeService;
    }

    @Override
    @Transactional
    public PostResponse publish(Long userId, PostCreateRequest request) {
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(request.getContent());
        post.setMarkdown(request.isMarkdown());
        post.setImages(request.getImages());
        post.setVisibility(request.getVisibility() == null ? PostVisibility.PUBLIC : request.getVisibility());
        post.setLikeCount(0);
        post.setCommentCount(0);
        save(post);
        return toResponse(post, userService.getById(userId), false);
    }

    @Override
    public PageResponse<PostResponse> listFeed(Long viewerId, Long userId, long page, long size) {
        LambdaQueryWrapper<Post> wrapper = buildVisibilityWrapper(viewerId, userId);
        wrapper.orderByDesc(Post::getCreatedAt);
        Page<Post> result = page(new Page<>(page, size), wrapper);
        List<Post> records = result.getRecords();
        if (records.isEmpty()) {
            return PageResponse.<PostResponse>builder()
                    .total(result.getTotal())
                    .page(page)
                    .size(size)
                    .records(Collections.emptyList())
                    .build();
        }
        Map<Long, User> userMap = userService.listByIds(
                        records.stream().map(Post::getUserId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(User::getId, Function.identity()));
        Set<Long> liked = postLikeService.queryLikedPostIds(viewerId,
                records.stream().map(Post::getId).toList());
        List<PostResponse> responses = records.stream()
                .map(post -> toResponse(post, userMap.get(post.getUserId()), liked.contains(post.getId())))
                .toList();
        return PageResponse.<PostResponse>builder()
                .total(result.getTotal())
                .page(page)
                .size(size)
                .records(responses)
                .build();
    }

    @Override
    public void increaseLike(Long postId, boolean increment) {
        lambdaUpdate()
                .eq(Post::getId, postId)
                .setSql("like_count = GREATEST(like_count " + (increment ? "+ 1" : "- 1") + ", 0)")
                .update();
    }

    @Override
    public void increaseComment(Long postId, boolean increment) {
        lambdaUpdate()
                .eq(Post::getId, postId)
                .setSql("comment_count = GREATEST(comment_count " + (increment ? "+ 1" : "- 1") + ", 0)")
                .update();
    }

    @Override
    public PostResponse findDetail(Long postId, Long viewerId) {
        Post post = getById(postId);
        if (post == null) {
            throw new BizException(ErrorCode.NOT_FOUND, "动态不存在");
        }
        boolean owner = Objects.equals(viewerId, post.getUserId());
        if (post.getVisibility() == PostVisibility.PRIVATE && !owner) {
            throw new BizException(ErrorCode.FORBIDDEN, "该动态仅作者可见");
        }
        boolean liked = viewerId != null && postLikeService.queryLikedPostIds(viewerId, List.of(postId)).contains(postId);
        return toResponse(post, userService.getById(post.getUserId()), liked);
    }

    private LambdaQueryWrapper<Post> buildVisibilityWrapper(Long viewerId, Long userId) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Post::getUserId, userId);
            if (!Objects.equals(viewerId, userId)) {
                wrapper.eq(Post::getVisibility, PostVisibility.PUBLIC);
            }
        } else if (viewerId == null) {
            wrapper.eq(Post::getVisibility, PostVisibility.PUBLIC);
        } else {
            wrapper.nested(w -> w.eq(Post::getVisibility, PostVisibility.PUBLIC)
                    .or()
                    .eq(Post::getUserId, viewerId));
        }
        return wrapper;
    }

    private PostResponse toResponse(Post post, User author, boolean liked) {
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .markdown(Boolean.TRUE.equals(post.getMarkdown()))
                .images(post.getImages())
                .visibility(post.getVisibility())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .liked(liked)
                .author(PostResponse.PostUserSummary.builder()
                        .id(author.getId())
                        .username(author.getUsername())
                        .avatarUrl(author.getAvatarUrl())
                        .build())
                .build();
    }
}

