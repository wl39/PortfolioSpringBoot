package com.wl39.portfolio.student;

import java.time.LocalDate;

public class StudentCalendar {
    private LocalDate date;
    private Long questions;
    private Long solved;
    private Long unmarked;

    public StudentCalendar(LocalDate date, Long questions, Long solved, Long unmarked) {
        this.date = date;
        this.questions = questions;
        this.solved = solved;
        this.unmarked = unmarked;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getQuestions() {
        return questions;
    }

    public void setQuestions(Long questions) {
        this.questions = questions;
    }

    public Long getSolved() {
        return solved;
    }

    public void setSolved(Long solved) {
        this.solved = solved;
    }

    public Long getUnmarked() {
        return unmarked;
    }

    public void setUnmarked(Long unmarked) {
        this.unmarked = unmarked;
    }
}
