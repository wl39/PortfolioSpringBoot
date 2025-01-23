package com.wl39.portfolio.assignment;

import java.time.LocalDateTime;
import java.util.List;

public class AssignmentDTO {
    private List<String> names;
    private List<Long> ids;
    private LocalDateTime targetDate;

    public AssignmentDTO(List<String> names, List<Long> ids, LocalDateTime targetDate) {
        this.names = names;
        this.ids = ids;
        this.targetDate = targetDate;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public LocalDateTime getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
    }
}
