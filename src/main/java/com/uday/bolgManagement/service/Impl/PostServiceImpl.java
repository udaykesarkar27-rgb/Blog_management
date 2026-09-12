package com.uday.bolgManagement.service.Impl;

import com.uday.bolgManagement.dto.PostCreateRequest;
import com.uday.bolgManagement.dto.PostResponse;
import com.uday.bolgManagement.dto.PostUpdateRequest;
import com.uday.bolgManagement.exception.ResourceNotFoundException;
import com.uday.bolgManagement.model.Post;
import com.uday.bolgManagement.model.User;
import com.uday.bolgManagement.repository.PostRepository;
import com.uday.bolgManagement.repository.UserRepository;
import com.uday.bolgManagement.service.PostService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PostResponse createPost(PostCreateRequest request) {
        User author = userRepository.findById(request.authorid())
                .orElseThrow(() -> new ResourceNotFoundException("User","id",request.authorid()));

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .author(author)
                .build();
        Post saved = postRepository.save(post);
        return mapToResponse(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(this:: mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        return mapToResponse(post);
    }

    @Override
    @Transactional
    public PostResponse updatePost(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",id));

        post.setTitle(request.title());
        post.setContent(request.content());

        Post updated = postRepository.save(post);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    private PostResponse mapToResponse(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getComments() != null ? post.getComments().size() :0

        );
    }
}
