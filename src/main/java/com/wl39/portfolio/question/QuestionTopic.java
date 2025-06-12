package com.wl39.portfolio.question;

import com.wl39.portfolio.topic.Topic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class QuestionTopic {
    private Long id;
    private String title;
    private String question;
    private Set<String> topics;

    public QuestionTopic(Long id, String title, String question, Set<String> topics) {
        this.id = id;
        this.title = title;
        this.question = question;
        this.topics = topics;
    }
}
