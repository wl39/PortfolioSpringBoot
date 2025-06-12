package com.wl39.portfolio.submission;

import com.wl39.portfolio.topic.Topic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@NoArgsConstructor
@Getter
@Setter
public class SubmissionTopic {
    private Long questionId;
    private Integer marked;
    private Set<String> topicTitles;
}
