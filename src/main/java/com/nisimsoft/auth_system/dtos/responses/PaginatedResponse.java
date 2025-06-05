package com.nisimsoft.auth_system.dtos.responses;

import java.util.List;

import org.springframework.data.domain.Page;

public record PaginatedResponse<T>(
        List<T> data,
        long total,
        int totalPages,
        int currentPage) {
    public static <T> PaginatedResponse<T> fromPage(Page<T> page) {
        return new PaginatedResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber() + 1);
    }
}