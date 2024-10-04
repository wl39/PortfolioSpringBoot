package com.wl39.portfolio.calendar;

import jakarta.persistence.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDate;

@Entity
@Table
public class Calendar {
    @EmbeddedId
    private CalendarID calendarID;

    @Column(nullable = false)
    private Integer questions = 0;
    @Column(nullable = false)
    private Integer solved = 0;
    @Column(nullable = false)
    private Integer unmarked = 0;

    public Calendar() {

    }

    public Calendar(CalendarID calendarID) {
        this.calendarID = calendarID;
        this.questions = 1;
        this.solved = 0;
        this.unmarked = 0;
    }

    public Calendar(CalendarID calendarID, Integer questions, Integer solved, Integer unmarked) {
        this.calendarID = calendarID;
        this.questions = questions;
        this.solved = solved;
        this.unmarked = unmarked;
    }

    public CalendarID getCalendarID() {
        return calendarID;
    }

    public void setCalendarID(CalendarID calendarID) {
        this.calendarID = calendarID;
    }

    public Integer getQuestions() {
        return questions;
    }

    public void setQuestions(Integer questions) {
        this.questions = questions;
    }

    public Integer getSolved() {
        return solved;
    }

    public void setSolved(Integer solved) {
        this.solved = solved;
    }

    public Integer getUnmarked() {
        return unmarked;
    }

    public void setUnmarked(Integer unmarked) {
        this.unmarked = unmarked;
    }
}
