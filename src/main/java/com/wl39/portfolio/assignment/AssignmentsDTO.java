package com.wl39.portfolio.assignment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class AssignmentsDTO {
    @NotNull
    private List<Long> questionIds;
    @NotNull
    private List<String> names;
    @NotNull
    private LocalDateTime targetDate;

    public AssignmentsDTO(List<Long> questionIds, List<String> names, LocalDateTime targetDate) {
        this.questionIds = questionIds;
        this.names = names;
        this.targetDate = targetDate;
    }

    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Long> questionIds) {
        this.questionIds = questionIds;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public LocalDateTime getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
    }
}
