package com.wl39.portfolio.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MessageDigest messageDigest;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        try {
            this.messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("ALGORITHM ERROR");
        }
    }

    public void signUpUser(String username, String password) {
        Optional<User> userOptional = this.userRepository.findUserByUsername(username);

        if (userOptional.isPresent()) {
            throw new IllegalStateException(username + " is already taken");
        }

        byte[] hashPassword = messageDigest.digest(password.getBytes());

        userRepository.save(new User(username, hashPassword));
    }

    public void loginUser(String username, String password) {
        Optional<byte[]> passwordOptional = this.userRepository.findPasswordByUsername(username);

        if (passwordOptional.isPresent()) {
            byte[] hashPassword = messageDigest.digest(password.getBytes());

            if (!Arrays.equals(hashPassword, passwordOptional.get())) {
                throw new IllegalStateException("LOGIN FAIL!");
            }
        } else {
            throw new IllegalStateException("NO CERTAIN USER");
        }

    }
}
