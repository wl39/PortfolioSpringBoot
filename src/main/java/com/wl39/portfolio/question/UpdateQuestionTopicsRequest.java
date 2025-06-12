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
public class UpdateQuestionTopicsRequest {
    private Long questionId;
    private List<String> topics;
}
