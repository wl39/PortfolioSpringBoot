package com.wl39.portfolio.submission;

import java.sql.Date;
import java.time.LocalDate;

public class SubmissionDayCount {
    public String name;
    public Long wrongCounts;
    public Long correctCounts;
    public LocalDate date;

    public SubmissionDayCount(String name, Long wrongCounts, Long correctCounts, Date date) {
        this.name = name;
        this.wrongCounts = wrongCounts;
        this.correctCounts = correctCounts;
        this.date = date.toLocalDate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWrongCounts() {
        return wrongCounts;
    }

    public void setWrongCounts(Long wrongCounts) {
        this.wrongCounts = wrongCounts;
    }

    public Long getCorrectCounts() {
        return correctCounts;
    }

    public void setCorrectCounts(Long correctCounts) {
        this.correctCounts = correctCounts;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
