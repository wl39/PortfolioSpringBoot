package com.wl39.portfolio.stopwatch;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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
        return time;
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
}
