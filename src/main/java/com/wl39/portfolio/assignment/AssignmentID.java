package com.wl39.portfolio.assignment;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class AssignmentID {
    private Long id;
    // Student's name
    private String name;
    private LocalDateTime date;

    public AssignmentID() {}

    public AssignmentID(Long id, String name, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
