package com.wl39.portfolio.submission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wl39.portfolio.question.Question;
import com.wl39.portfolio.question.QuestionOnly;
import com.wl39.portfolio.student.Student;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class SubmissionQuestion {
    private Long id;

    private QuestionOnly question;

    private String studentAnswer;

    private LocalDateTime submitDate;
    private LocalDateTime targetDate;
    private Integer marked;

    public SubmissionQuestion(Long id, QuestionOnly question, String studentAnswer, LocalDateTime submitDate, Integer marked) {
        this.id = id;
        this.question = question;
        this.studentAnswer = studentAnswer;
        this.submitDate = submitDate;
        this.marked = marked;
    }

    public SubmissionQuestion(Long id, Question question, String studentAnswer, LocalDateTime submitDate, LocalDateTime targetDate, Integer marked) {
        this.id = id;
        this.question = new QuestionOnly(
                question.getId(),
                question.getTitle(),
                question.getQuestion(),
                question.getType(),
                question.getCandidates(),
                question.getHint(),
                question.getAnswer(),
                question.getExplanation(),
                question.getGeneratedDate(),
                question.getTopics()
        );;
        this.studentAnswer = studentAnswer;
        this.submitDate = submitDate;
        this.targetDate = targetDate;
        this.marked = marked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuestionOnly getQuestion() {
        return question;
    }

    public void setQuestion(QuestionOnly question) {
        this.question = question;
    }

    public void setQuestion(Question question) {
        this.question = new QuestionOnly(
                question.getId(),
                question.getTitle(),
                question.getQuestion(),
                question.getType(),
                question.getCandidates(),
                question.getHint(),
                question.getAnswer(),
                question.getExplanation(),
                question.getGeneratedDate(),
                question.getTopics()
        );
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public LocalDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(LocalDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public LocalDateTime getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
    }

    public Integer getMarked() {
        return marked;
    }

    public void setMarked(Integer marked) {
        this.marked = marked;
    }
}
