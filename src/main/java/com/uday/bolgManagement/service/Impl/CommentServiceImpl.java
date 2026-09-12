package com.uday.bolgManagement.service.Impl;

import com.uday.bolgManagement.dto.CommentRequest;
import com.uday.bolgManagement.dto.CommentResponse;
import com.uday.bolgManagement.exception.ResourceNotFoundException;
import com.uday.bolgManagement.model.Comment;
import com.uday.bolgManagement.model.Post;
import com.uday.bolgManagement.model.User;
import com.uday.bolgManagement.repository.CommentRepository;
import com.uday.bolgManagement.repository.PostRepository;
import com.uday.bolgManagement.repository.UserRepository;
import com.uday.bolgManagement.service.CommentService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentResponse addComment(Long postId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","Id",postId));

        User author = userRepository.findById(request.authorId())
                .orElseThrow(()-> new ResourceNotFoundException("User","id", request.authorId()));

        Comment comment = Comment.builder()
                .content(request.content())
                .post(post)
                .author(author)
                .build();
        Comment saved = commentRepository.save(comment);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentByPostId(Long postId) {
        if (!postRepository.existsById(postId)){
            throw new ResourceNotFoundException("Post","id",postId);
        }
        return commentRepository.findByPostId(postId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id", commentId));
        commentRepository.delete(comment);
    }

    private  final CommentResponse mapToResponse(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getPost().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getUsername(),
                comment.getCreatedAt()
        );
    }
}
