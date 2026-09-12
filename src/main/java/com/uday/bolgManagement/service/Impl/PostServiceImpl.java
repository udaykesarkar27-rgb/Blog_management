package com.uday.bolgManagement.service.Impl;

import com.uday.bolgManagement.dto.PostCreateRequest;
import com.uday.bolgManagement.dto.PostResponse;
import com.uday.bolgManagement.dto.PostUpdateRequest;
import com.uday.bolgManagement.exception.ResourceNotFoundException;
import com.uday.bolgManagement.exception.UnauthorizedActionException;
import com.uday.bolgManagement.model.Post;
import com.uday.bolgManagement.model.Role;
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
    public PostResponse createPost(PostCreateRequest request,String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User","username",username));

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
    public PostResponse updatePost(Long id, PostUpdateRequest request,String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",id));

        User currentUser = userRepository.findByUsername(username)
                        .orElseThrow(()->new ResourceNotFoundException("User","username",username));

        //Rule: Only the original author can edit this post
        if (!java.util.Objects.equals(post.getAuthor().getId(), currentUser.getId())) {
            throw new UnauthorizedActionException("You are not authorized to update this post");
        }

        post.setTitle(request.title());
        post.setContent(request.content());

        Post updated = postRepository.save(post);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deletePost(Long id,String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Post", "id", id));
        User curentUser = userRepository.findByUsername(username)
                        .orElseThrow(()->new ResourceNotFoundException("User","user",username));

        //Rule: Either the original Author or An Admin can delete the post.
        boolean isadmin = curentUser.getRole() == Role.ROLE_Admin;
        boolean isauthor = java.util.Objects.equals(curentUser.getId(),post.getAuthor().getId());

        if (!isadmin && !isauthor){
            throw new UnauthorizedActionException("You are not authorized to delete this post!");
        }
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
