package com.blog.modules.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.modules.comment.dto.CommentRequest;
import com.blog.modules.comment.dto.CommentResponse;
import com.blog.modules.comment.entity.Comment;
import com.blog.modules.comment.mapper.CommentMapper;
import com.blog.modules.comment.service.CommentService;
import com.blog.modules.post.service.PostService;
import com.blog.modules.user.entity.User;
import com.blog.modules.user.service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final UserService userService;
    private final PostService postService;

    public CommentServiceImpl(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @Override
    public CommentResponse addComment(Long userId, Long postId, CommentRequest request) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setPostId(postId);
        comment.setParentId(request.getParentId());
        comment.setContent(request.getContent());
        comment.setCreatedAt(java.time.LocalDateTime.now());
        save(comment);
        postService.increaseComment(postId, true);
        return buildResponse(comment, userService.getById(userId));
    }

    @Override
    public List<CommentResponse> listTreeByPost(Long postId) {
        List<Comment> comments = lambdaQuery()
                .eq(Comment::getPostId, postId)
                .orderByAsc(Comment::getCreatedAt)
                .list();
        if (comments.isEmpty()) {
            return List.of();
        }
        Map<Long, User> userMap = userService.listByIds(
                        comments.stream().map(Comment::getUserId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(User::getId, user -> user));

        Map<Long, CommentResponse> responseMap = new HashMap<>();
        List<CommentResponse> roots = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponse response = buildResponse(comment, userMap.get(comment.getUserId()));
            responseMap.put(comment.getId(), response);
            if (comment.getParentId() == null) {
                roots.add(response);
            } else {
                CommentResponse parent = responseMap.get(comment.getParentId());
                if (parent != null) {
                    parent.getReplies().add(response);
                } else {
                    roots.add(response);
                }
            }
        }
        return roots;
    }

    private CommentResponse buildResponse(Comment comment, User user) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .parentId(comment.getParentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .author(CommentResponse.AuthorSummary.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .avatarUrl(user.getAvatarUrl())
                        .build())
                .build();
    }
}

