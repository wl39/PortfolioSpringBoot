package com.wl39.portfolio.assignment;

import com.wl39.portfolio.question.Question;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name ="assignment")
public class Assignment {
    @EmbeddedId
    private AssignmentID id;

    @MapsId("questionId")
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    private LocalDateTime targetDate;

    public Assignment() {}

    public Assignment(AssignmentID id, LocalDateTime targetDate) {
        this.id = id;
        this.targetDate = targetDate;
    }

    public AssignmentID getId() {
        return id;
    }

    public void setId(AssignmentID id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public LocalDateTime getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
    }

    public void setDataFromQuestion(Question question) {
        this.question = question;
        this.targetDate = question.getTargetDate();
        this.id.setGeneratedDate(question.getGeneratedDate());
    }
}
