package com.wl39.portfolio.question;

import com.wl39.portfolio.candidate.Candidate;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class QuestionStudentDTO {
    private Long id;
    private String title;
    private String question;
    private Character type;
    private List<Candidate> candidates;
    private String hint;
    private LocalDateTime generatedDate;
    private LocalDateTime targetDate;
    @Transient
    private Long minAgo;
    @Transient
    private Long hourAgo;
    @Transient
    private Long dayAgo;
    @Transient
    private Long minLeft;
    @Transient
    private Long hourLeft;
    @Transient
    private Long dayLeft;

    public QuestionStudentDTO(Long id, String title, String question, Character type, List<Candidate> candidates, String hint, LocalDateTime generatedDate, LocalDateTime targetDate) {
        this.id = id;
        this.title = title;
        this.question = question;
        this.type = type;
        this.candidates = candidates;
        this.hint = hint;
        this.generatedDate = generatedDate;
        this.targetDate = targetDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
    }

    public LocalDateTime getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
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

    public Long getMinLeft() {
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), this.targetDate);
    }

    public void setMinLeft(Long minLeft) {
        this.minLeft = minLeft;
    }

    public Long getHourLeft() {
        return ChronoUnit.HOURS.between(LocalDateTime.now(), this.targetDate);
    }

    public void setHourLeft(Long hourLeft) {
        this.hourLeft = hourLeft;
    }

    public Long getDayLeft() {
        return ChronoUnit.DAYS.between(LocalDateTime.now(), this.targetDate);
    }

    public void setDayLeft(Long dayLeft) {
        this.dayLeft = dayLeft;
    }
}
