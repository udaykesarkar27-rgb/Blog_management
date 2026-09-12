package com.uday.bolgManagement.controller;

import com.uday.bolgManagement.dto.PostCreateRequest;
import com.uday.bolgManagement.dto.PostResponse;
import com.uday.bolgManagement.dto.PostUpdateRequest;
import com.uday.bolgManagement.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails){ // from where we got userdetails.
        PostResponse created = postService.createPost(request,userDetails.getUsername()); //we store the response from createPost() method
        //and store it in 'created' so that we can return it.
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @PageableDefault(page = 0,size = 10,sort = "createdAt",direction = Sort.Direction.DESC)Pageable pageable){
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(postService.updatePost(id,request,userDetails.getUsername()));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails){
        postService.deletePost(id,userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }


}
