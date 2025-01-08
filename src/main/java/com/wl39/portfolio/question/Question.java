package com.wl39.portfolio.question;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wl39.portfolio.assignment.Assignment;
import com.wl39.portfolio.candidate.Candidate;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 10000)
    private String question;
    private Character type;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private List<Candidate> candidates;
    private String hint;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private List<Assignment> assignments;

    private String answer;
    private String explanation;
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

    public Question() {}

    public Question(String title, String question, Character type, List<Candidate> candidates, String hint, List<Assignment> assignments, String answer, String explanation, LocalDateTime generatedDate, LocalDateTime targetDate) {
        this.title = title;
        this.question = question;
        this.type = type;
        this.candidates = candidates;
        this.assignments = assignments;
        this.hint = hint;
        this.answer = answer;
        this.explanation = explanation;
        this.generatedDate = generatedDate;
        this.targetDate = targetDate;
    }

    public Long getId() {
        System.out.println();
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
        return candidates == null ? new ArrayList<>() : candidates;
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

    public List<Assignment> getAssignments() {
        return assignments == null ? new ArrayList<>() : assignments;
    }
    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
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

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", question='" + question + '\'' +
                ", type=" + type +
                ", candidates=" + candidates +
                ", hint='" + hint + '\'' +
                ", answer='" + answer + '\'' +
                ", explanation='" + explanation + '\'' +
                ", generatedDate=" + generatedDate +
                ", targetDate=" + targetDate +
                ", minAgo=" + minAgo +
                ", hourAgo=" + hourAgo +
                ", dayAgo=" + dayAgo +
                ", minLeft=" + minLeft +
                ", hourLeft=" + hourLeft +
                ", dayLeft=" + dayLeft +
                '}';
    }
}
