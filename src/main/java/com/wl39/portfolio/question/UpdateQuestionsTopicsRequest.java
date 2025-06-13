package com.wl39.portfolio.question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuestionsTopicsRequest {
    private List<Long> questionIds;
    private List<String> topics;
}
