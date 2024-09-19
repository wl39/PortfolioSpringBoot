package com.wl39.portfolio.submission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wl39.portfolio.question.Question;
import jakarta.persistence.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;

@Entity
@Table
public class Submission {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private String studentAnswer;
    private String studentName;
    private LocalDateTime submitDate;

    private Boolean marked;

    public Submission() {

    }

    public Submission(Question question, String studentAnswer, String studentName, LocalDateTime submitDate) {
        this.question = question;
        this.studentAnswer = studentAnswer;
        this.studentName = studentName;
        this.submitDate = submitDate;
    }

    public Submission(Long id, Question question, String studentAnswer, String studentName, LocalDateTime submitDate, Boolean marked) {
        this.id = id;
        this.question = question;
        this.studentAnswer = studentAnswer;
        this.studentName = studentName;
        this.submitDate = submitDate;
        this.marked = marked;
    }

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(LocalDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }
}
