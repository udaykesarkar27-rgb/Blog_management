package com.uday.bolgManagement.repository;

import com.uday.bolgManagement.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment ,Long> {
    List<Comment> findByPostId(Long postId);
}
