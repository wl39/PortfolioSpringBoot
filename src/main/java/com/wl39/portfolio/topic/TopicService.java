package com.wl39.portfolio.topic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;

    public ResponseEntity<?> getAllTopics() {
        return ResponseEntity.ok(topicRepository.findAll());
    }

    public ResponseEntity<?> postTopic(String title) {
        Topic topic = new Topic();
        topic.setTitle(title);

        return ResponseEntity.ok(topicRepository.save(topic));
    }
}
