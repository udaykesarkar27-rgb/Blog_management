package com.uday.bolgManagement.service;

import com.uday.bolgManagement.dto.CommentRequest;
import com.uday.bolgManagement.dto.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse addComment(Long postId, CommentRequest request,String username);
    List<CommentResponse> getCommentByPostId(Long postId);
    void deleteComment(Long commentId,String username);
}
