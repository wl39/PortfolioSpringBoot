package com.wl39.portfolio.post;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class PostConfig {

    @Bean
    CommandLineRunner postCommandLineRunner(PostRepository repository) {
        return args -> {
            Post post1 = new Post("My first post!", "Lim", LocalDateTime.of(2023, 10, 31, 20, 57, 20), LocalDateTime.of(2023, 10, 31, 20, 57, 20), "This is the first post");
            Post post2 = new Post("My second post!", "Lima", LocalDateTime.of(2023, 11, 1, 17, 33, 8), LocalDateTime.of(2023, 11, 1, 17, 33, 8), "This is the second post");

            repository.saveAll(List.of(post1, post2));
        };
    }
}
