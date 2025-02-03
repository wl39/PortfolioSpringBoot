package com.wl39.portfolio.question;

import com.wl39.portfolio.candidate.Candidate;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionOnly {
    private Long id;
    private String title;
    private String question;
    private Character type;
    private List<Candidate> candidates;
    private String hint;
    private String answer;
    private String explanation;
    private LocalDateTime generatedDate;


    public QuestionOnly(Long id, String title, String question, Character type, List<Candidate> candidates, String hint, String answer, String explanation, LocalDateTime generatedDate) {
        this.id = id;
        this.title = title;
        this.question = question;
        this.type = type;
        this.candidates = candidates;
        this.hint = hint;
        this.answer = answer;
        this.explanation = explanation;
        this.generatedDate = generatedDate;
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
}
