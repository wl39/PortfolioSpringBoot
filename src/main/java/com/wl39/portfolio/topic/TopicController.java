package com.wl39.portfolio.topic;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> postTopic(@RequestBody @Valid String title) {
        return topicService.postTopic(title);
    }
}
