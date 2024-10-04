package com.wl39.portfolio.calendar;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class CalendarID implements Serializable {

    private LocalDate date;
    private String studentName;

    public CalendarID() {
    }

    public CalendarID(LocalDate date, String studentName) {
        this.date = date;
        this.studentName = studentName;
    }

    // Getters, Setters, equals, and hashCode methods
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarID that = (CalendarID) o;
        return Objects.equals(date, that.date) && Objects.equals(studentName, that.studentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, studentName);
    }
}
