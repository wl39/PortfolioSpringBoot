package com.wl39.portfolio.submission;

import java.time.LocalDateTime;

public class SubmissionDTO {
    private Long questionId;
    private String studentAnswer;
    private String studentName;
    private LocalDateTime submitDate;
    private Boolean marked = true;

    public SubmissionDTO(Long questionId, String studentAnswer, String studentName, LocalDateTime submitDate) {
        this.questionId = questionId;
        this.studentAnswer = studentAnswer;
        this.studentName = studentName;
        this.submitDate = submitDate;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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
