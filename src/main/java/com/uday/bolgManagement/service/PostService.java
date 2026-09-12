package com.uday.bolgManagement.service;

import com.uday.bolgManagement.dto.PostCreateRequest;
import com.uday.bolgManagement.dto.PostResponse;
import com.uday.bolgManagement.dto.PostUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PostResponse createPost(PostCreateRequest request ,String username);
    Page<PostResponse> getAllPosts(Pageable pageable);
    PostResponse getPostById(Long id);
    PostResponse updatePost(Long id , PostUpdateRequest request,String username);
    void deletePost(Long id,String username);
}
