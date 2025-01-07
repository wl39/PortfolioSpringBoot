package com.wl39.portfolio.assignment;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name ="assignment")
public class

Assignment {
    @EmbeddedId
    private AssignmentID id;

    private LocalDateTime targetDate;

    public Assignment() {}

    public Assignment(AssignmentID id, LocalDateTime targetDate) {
        this.id = id;
        this.targetDate = targetDate;
    }

    public AssignmentID getId() {
        return id;
    }

    public void setId(AssignmentID id) {
        this.id = id;
    }

    public LocalDateTime getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
    }
}
