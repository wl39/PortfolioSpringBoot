package com.wl39.portfolio.submission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wl39.portfolio.question.Question;
import com.wl39.portfolio.student.Student;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class Submission {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private String studentAnswer;
    private LocalDateTime submitDate;
    private LocalDateTime targetDate;

    private Integer marked;

    public Submission() {

    }

    public Submission(Question question, String studentAnswer, LocalDateTime submitDate, LocalDateTime targetDate) {
        this.question = question;
        this.studentAnswer = studentAnswer;
        this.submitDate = submitDate;
        this.targetDate = targetDate;
    }

    public Submission(Question question, Student student,
                      String studentAnswer, LocalDateTime submitDate,
                      LocalDateTime targetDate) {
        this.question = question;
        this.student = student;
        this.studentAnswer = studentAnswer;
        this.submitDate = submitDate;
        this.targetDate = targetDate;
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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
