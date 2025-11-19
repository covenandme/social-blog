package com.blog.common.exception;

import com.blog.common.api.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ApiResponse<Void> handleBiz(BizException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        return ApiResponse.failure(ex.getErrorCode().getCode(), ex.getMessage());
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class
    })
    public ApiResponse<Void> handleValidation(Exception ex) {
        log.warn("参数异常", ex);
        return ApiResponse.failure(ErrorCode.VALIDATION_FAILED.getCode(), "参数错误");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse<Void> handleAuth(AuthenticationException ex) {
        return ApiResponse.failure(ErrorCode.UNAUTHORIZED.getCode(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<Void> handleAccess(AccessDeniedException ex) {
        return ApiResponse.failure(ErrorCode.FORBIDDEN.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleOther(Exception ex) {
        log.error("系统异常", ex);
        return ApiResponse.failure(ErrorCode.SERVER_ERROR.getCode(), "系统异常");
    }
}

