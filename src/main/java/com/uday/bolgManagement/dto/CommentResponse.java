package com.uday.bolgManagement.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        Long postId,
        Long authorId,
        String authorUserName,
        LocalDateTime createdAt
) {}
