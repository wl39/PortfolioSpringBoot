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
public class QuestionOnly {
    private Long id;
    private String title;
    private String question;
    private Character type;
    private List<Candidate> candidates;
    private String hint;
    private String answer;
    private String explanation;
    private LocalDateTime generatedDate;
    private Set<String> topicTitles;


    public QuestionOnly(Long id, String title, String question, Character type, List<Candidate> candidates, String hint, String answer, String explanation, LocalDateTime generatedDate, Set<Topic> topics) {
        this.id = id;
        this.title = title;
        this.question = question;
        this.type = type;
        this.candidates = candidates;
        this.hint = hint;
        this.answer = answer;
        this.explanation = explanation;
        this.generatedDate = generatedDate;
        this.topicTitles = topics.stream()
                .map(Topic::getTitle)
                .collect(Collectors.toSet());
    }
}
