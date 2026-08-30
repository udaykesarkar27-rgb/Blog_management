package com.uday.bolgManagement.repository;

import com.uday.bolgManagement.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Marks the interface as a Spring Data Data Access Object (DAO) component.
public interface PostRepository extends JpaRepository<Post ,Long> {
    Page<Post> findByAuthorId(Long authorId, Pageable pageable);

    //Instead of loading thousands or millions of records into memory at once, passing a Pageable
    // instance instructs the database to fetch only a specific "slice" or "page" of rows

}
