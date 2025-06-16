package com.wl39.portfolio.topic;

import com.wl39.portfolio.user.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/topics")
@RequiredArgsConstructor
@Validated
public class TopicController {
    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<?> getAllTopics() {
        return topicService.getAllTopics();
    }

    @PostMapping
    public ResponseEntity<?> postTopic(@RequestBody @Valid String title, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return topicService.postTopic(title);
    }
}
