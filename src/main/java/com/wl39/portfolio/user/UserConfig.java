package com.wl39.portfolio.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;

@Configuration
public class UserConfig {
    @Bean
    CommandLineRunner userCommandLineRunner(UserRepository repository) {
        return args -> {
            MessageDigest md = MessageDigest.getInstance("MD5");
            User user = new User("lim", md.digest("limw6852".getBytes()));

            repository.save(user);
        };
    }
}
