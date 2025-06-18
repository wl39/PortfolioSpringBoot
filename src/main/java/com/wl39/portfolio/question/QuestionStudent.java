package com.wl39.portfolio.question;

import com.wl39.portfolio.candidate.Candidate;
import com.wl39.portfolio.topic.Topic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class QuestionStudent {
    private Long id;
    private String title;
    private String question;
    private Character type;
    private List<Candidate> candidates;
    private String hint;
    private LocalDateTime generatedDate;
    private Set<String> topics;

    public QuestionStudent(Long id, String title, String question, Character type, List<Candidate> candidates, String hint, LocalDateTime generatedDate, Set<Topic> topics) {
        this.id = id;
        this.title = title;
        this.question = question;
        this.type = type;
        this.candidates = candidates;
        this.hint = hint;
        this.generatedDate = generatedDate;
        this.topics = topics.stream()
                .map(Topic::getTitle)
                .collect(Collectors.toSet());
    }
}
