package com.uday.bolgManagement.controller;

import com.uday.bolgManagement.dto.CommentRequest;
import com.uday.bolgManagement.dto.CommentResponse;
import com.uday.bolgManagement.model.Comment;
import com.uday.bolgManagement.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request){
        CommentResponse created = commentService.addComment(postId, request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId){
    return ResponseEntity.ok(commentService.getCommentByPostId(postId));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
    commentService.deleteComment(id);
    return ResponseEntity.noContent().build();
    }
}
