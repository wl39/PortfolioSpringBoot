package com.wl39.portfolio.assignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wl39.portfolio.question.Question;
import com.wl39.portfolio.student.Student;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name ="assignment")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @JsonIgnore
    private Question question;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    private LocalDateTime targetDate;

    @Transient
    private Long minLeft;
    @Transient
    private Long hourLeft;
    @Transient
    private Long dayLeft;

    public Assignment() {}

    public Assignment(Question question, Student student, LocalDateTime targetDate) {
        this.question = question;
        this.student = student;
        this.targetDate = targetDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
    }

    public void setDataFromQuestion(Question question) {
        this.question = question;
    }
//    public Long getMinAgo() {
//        return ChronoUnit.MINUTES.between(this.generatedDate, LocalDateTime.now());
//    }
//
//    public void setMinAgo(Long minAgo) {
//        this.minAgo = minAgo;
//    }
//
//    public Long getHourAgo() {
//        return ChronoUnit.HOURS.between(this.generatedDate, LocalDateTime.now());
//    }
//
//    public void setHourAgo(Long hourAgo) {
//        this.hourAgo = hourAgo;
//    }
//
//    public Long getDayAgo() {
//        return ChronoUnit.DAYS.between(this.generatedDate, LocalDateTime.now());
//    }
//
//    public void setDayAgo(Long dayAgo) {
//        this.dayAgo = dayAgo;
//    }
    public Long getMinLeft() {
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), this.targetDate);
    }

    public void setMinLeft(Long minLeft) {
        this.minLeft = minLeft;
    }

    public Long getHourLeft() {
        return ChronoUnit.HOURS.between(LocalDateTime.now(), this.targetDate);
    }

    public void setHourLeft(Long hourLeft) {
        this.hourLeft = hourLeft;
    }

    public Long getDayLeft() {
        return ChronoUnit.DAYS.between(LocalDateTime.now(), this.targetDate);
    }

    public void setDayLeft(Long dayLeft) {
        this.dayLeft = dayLeft;
    }
}
