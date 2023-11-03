package com.wl39.portfolio.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // SELECT * FROM Post;
    @Query("SELECT p FROM Post p WHERE p.author = ?1")
    Optional<Post> findPostByAuthor(String author);
}
