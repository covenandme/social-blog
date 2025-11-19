package com.blog.common.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> ApiResponse<T> success(T body) {
        return ApiResponse.<T>builder()
                .code(0)
                .msg("success")
                .data(body)
                .build();
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> failure(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .msg(message)
                .build();
    }
}

