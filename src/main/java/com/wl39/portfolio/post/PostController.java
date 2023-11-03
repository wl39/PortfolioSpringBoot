package com.wl39.portfolio.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return this.postService.getAllPosts();
    }

    @PostMapping
    public void uploadPost(@RequestBody Post post) {
        this.postService.uploadPost(post);
    }

    @DeleteMapping(path = "{postID}")
    public void deletePost(@PathVariable("postID") Long postID) {
        this.postService.deletePost(postID);
    }

    @PutMapping(path = "{postID}")
    public void updatePost(@PathVariable("postID") Long postID, @RequestBody(required = true) String paragraph) {
        this.postService.updatePost(postID, paragraph);
    }
}
