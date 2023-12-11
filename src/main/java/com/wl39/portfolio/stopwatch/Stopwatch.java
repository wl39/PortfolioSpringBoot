package com.wl39.portfolio.stopwatch;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table
public class Stopwatch {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private Long relatedID;

    private String name;
    private LocalDateTime generatedDate;
    private Long elapsedTime;
    private Character type;
    @Transient
    private String time;
    @Transient
    private Long minAgo;
    @Transient
    private Long hourAgo;
    @Transient
    private Long dayAgo;
    @Transient
    private Long monthAgo;
    @Transient
    private Long yearAgo;

    public Stopwatch() {

    }

    public Stopwatch(Long id, Long relatedID, String name, LocalDateTime generatedDate, Long elapsedTime, Character type, String time) {
        this.id = id;
        this.relatedID = relatedID;
        this.name = name;
        this.generatedDate = generatedDate;
        this.elapsedTime = elapsedTime;
        this.time = time;
    }

    public Stopwatch(String name, LocalDateTime generatedDate, Long elapsedTime, Character type) {
        this.name = name;
        this.generatedDate = generatedDate;
        this.elapsedTime = elapsedTime;
        this.type = type;
    }

    public Stopwatch(String name, Long relatedID, LocalDateTime generatedDate, Long elapsedTime, Character type) {
        this.name = name;
        this.relatedID = relatedID;
        this.generatedDate = generatedDate;
        this.elapsedTime = elapsedTime;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRelatedID() {
        return relatedID;
    }

    public void setRelatedID(Long relatedID) {
        this.relatedID = relatedID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getTime() {
        long hours = elapsedTime / 360000;
        long minutes = (elapsedTime / 6000) % 60;
        long seconds = (elapsedTime / 100) % 60;

        return hours +
                ":" +
                minutes +
                ":" +
                seconds;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public Long getMinAgo() {
        return ChronoUnit.MINUTES.between(this.generatedDate, LocalDateTime.now());
    }

    public void setMinAgo(Long minAgo) {
        this.minAgo = minAgo;
    }

    public Long getHourAgo() {
        return ChronoUnit.HOURS.between(this.generatedDate, LocalDateTime.now());
    }

    public void setHourAgo(Long hourAgo) {
        this.hourAgo = hourAgo;
    }

    public Long getDayAgo() {
        return ChronoUnit.DAYS.between(this.generatedDate, LocalDateTime.now());
    }

    public void setDayAgo(Long dayAgo) {
        this.dayAgo = dayAgo;
    }

    public Long getMonthAgo() {
        return ChronoUnit.MONTHS.between(this.generatedDate, LocalDateTime.now());
    }

    public void setMonthAgo(Long monthAgo) {
        this.monthAgo = monthAgo;
    }

    public Long getYearAgo() {
        return ChronoUnit.YEARS.between(this.generatedDate, LocalDateTime.now());
    }

    public void setYearAgo(Long yearAgo) {
        this.dayAgo = yearAgo;
    }
}
