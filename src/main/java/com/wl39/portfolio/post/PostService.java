package com.wl39.portfolio.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public List<Post> getRecentPosts() {
        return List.of(
                new Post(1L, "My first post", "Lim", LocalDateTime.of(2023, 10, 31, 20, 57, 20), LocalDateTime.of(2023, 10, 31, 20, 57, 20), "This is my first post! Thank you!")
        );
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void uploadPost(Post post) {
        Optional<Post> postOptional = this.postRepository.findPostByAuthor(post.getAuthor());

        if (!postOptional.isPresent()) {
            throw new IllegalStateException("AUTHOR IS MISSING");
        }

        postRepository.save(post);
    }

    public void deletePost(Long postID) {
        if (!postRepository.existsById(postID)) {
            throw new IllegalStateException("Post with ID (" + postID + ") does not exist");
        }

        postRepository.deleteById(postID);
    }

    @Transactional
    public void updatePost(Long postID, String paragraph) {
        Post post = postRepository.findById(postID).orElseThrow(() -> {
            return new IllegalStateException("Post with ID (" + postID + ") does not exist");
        });

        if (paragraph != null && paragraph.length() > 0 && !paragraph.equals(post.getParagraph())) {
            post.setParagraph(paragraph);
        }
    }
}
