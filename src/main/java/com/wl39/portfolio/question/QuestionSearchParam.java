package com.wl39.portfolio.question;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class QuestionSearchParam {
    private Long id;
    private Boolean solved;
    private String title;
    private String question;
    private LocalDate targetDate;
    private List<String> topics;
}
