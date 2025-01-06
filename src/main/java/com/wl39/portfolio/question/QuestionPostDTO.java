package com.wl39.portfolio.question;

import com.wl39.portfolio.candidate.Candidate;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionPostDTO {
    private String title;
    private String question;
    private Character type;
    private List<Candidate> candidates;
    private String hint;
    private LocalDateTime generatedDate;
    private LocalDateTime targetDate;
}
