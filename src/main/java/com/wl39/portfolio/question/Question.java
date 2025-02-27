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
    private List<Candidate> candidates;
    private String hint;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments;

    @Column(length = 1000)
    private String answer;

    @Column(length = 2000)
    private String explanation;
    private LocalDateTime generatedDate;
    public Question() {}

    public Question(String title, String question, Character type, List<Candidate> candidates, String hint, List<Assignment> assignments, String answer, String explanation, LocalDateTime generatedDate) {
        this.title = title;
        this.question = question;
        this.type = type;
        this.candidates = candidates;
        this.assignments = assignments;
        this.hint = hint;
        this.answer = answer;
        this.explanation = explanation;
        this.generatedDate = generatedDate;
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

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDateTime generatedDate) {
        this.generatedDate = generatedDate;
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
                '}';
    }
}
