package com.wl39.portfolio.candidate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wl39.portfolio.question.Question;
import jakarta.persistence.*;

@Entity
@Table(name = "candidate")
@JsonIgnoreProperties({"question"})
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10000)
    private String value;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Candidate() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
