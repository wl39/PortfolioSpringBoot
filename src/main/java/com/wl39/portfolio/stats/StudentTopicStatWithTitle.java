package com.wl39.portfolio.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentTopicStatWithTitle {
    private String topic;
    private Long correctCount;
    private Long wrongCount;
    private Long totalCount;
}
