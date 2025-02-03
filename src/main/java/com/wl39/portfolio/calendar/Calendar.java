package com.wl39.portfolio.calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wl39.portfolio.assignment.Assignment;
import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.submission.Submission;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;
    private Integer month;
    private Integer day;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private Long solved;
    private Long unsolved;
    private Long toMark;

    public Calendar(Integer year, Integer month, Integer day, Student student, Long solved, Long unsolved, Long toMark) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.student = student;
        this.solved = solved;
        this.unsolved = unsolved;
        this.toMark = toMark;
    }


    public Calendar() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Long getSolved() {
        return solved;
    }

    public void setSolved(Long solved) {
        this.solved = solved;
    }

    public Long getUnsolved() {
        return unsolved;
    }

    public void setUnsolved(Long unsolved) {
        this.unsolved = unsolved;
    }

    public Long getToMark() {
        return toMark;
    }

    public void setToMark(Long toMark) {
        this.toMark = toMark;
    }
}
