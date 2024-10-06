package com.wl39.portfolio.question;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionStudentsDTO {
    private List<Long> questionIds;
    private List<String> studentsFor;
    private LocalDateTime targetDate;

    // Getters and setters
    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Long> questionIds) {
        this.questionIds = questionIds;
    }

    public List<String> getStudentsFor() {
        return studentsFor;
    }

    public void setStudentsFor(List<String> studentsFor) {
        this.studentsFor = studentsFor;
    }

    public LocalDateTime getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
    }
}
