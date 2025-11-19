package com.blog.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED(401, "未认证，请重新登录"),
    FORBIDDEN(403, "无权操作"),
    NOT_FOUND(404, "资源不存在"),
    VALIDATION_FAILED(422, "请求参数错误"),
    CONFLICT(409, "资源冲突"),
    SERVER_ERROR(500, "系统开小差了"),
    BUSINESS_ERROR(400, "业务异常");

    private final int code;
    private final String message;
}

