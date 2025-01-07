package com.wl39.portfolio.assignment;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class AssignmentID {

    private Long questionId;
    private String name;
    private LocalDateTime generatedDate;

    public AssignmentID() {}

    // Getters and setters
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentID that = (AssignmentID) o;
        return
                Objects.equals(questionId, that.questionId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(generatedDate, that.generatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                questionId,
                name, generatedDate);
    }
}
