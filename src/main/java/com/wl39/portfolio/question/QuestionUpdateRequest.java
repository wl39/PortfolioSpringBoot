package com.wl39.portfolio.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionUpdateRequest {
    @NotNull(message = "Id must not be blank")
    private Long id;

    private String title;

    private String question;

    private Character type;

    private String hint;

    private String answer;

    private String explanation;

    private LocalDateTime generatedDate;

    private LocalDateTime targetDate;

    private List<String> candidates;

    private List<String> students;
    private List<String> topics;
}
