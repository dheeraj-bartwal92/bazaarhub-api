package com.bazaarhub.common;

import java.time.Instant;
import java.util.List;

public record ApiResponse<T, E>(
        boolean success,
        String message,
        T data,
        List<E> errors,
        Instant timestamp
) {
    public static <T, E> ApiResponse<T, E> success(T data) {
        return new ApiResponse<>(true, "Success", data, null, Instant.now());
    }

    public static <T, E> ApiResponse<T, E> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null, Instant.now());
    }

    public static <T, E> ApiResponse<T, E> error(String message) {
        return new ApiResponse<>(false, message, null, null, Instant.now());
    }

    public static <T, E> ApiResponse<T, E> error(String message, List<E> errors) {
        return new ApiResponse<>(false, message, null, errors, Instant.now());
    }
}