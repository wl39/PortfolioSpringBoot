package com.wl39.portfolio.question;

import com.wl39.portfolio.candidate.Candidate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionCreateRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Question must not be blank")
    private String question;

    @NotNull(message = "Type must not be null")
    private Character type;

    private String hint;

    @NotBlank(message = "Answer must not be blank")
    private String answer;

    @NotBlank(message = "Explanation must not be blank")
    private String explanation;

    @NotNull(message = "Generated date must not be null")
    private LocalDateTime generatedDate;

    @NotNull(message = "Target date must not be null")
    private LocalDateTime targetDate;

    @NotNull(message = "Candidates list must not be null")
    private List<String> candidates;

    @NotNull(message = "Students list must not be null")
    private List<String> students;


    public QuestionCreateRequest() {}

    public QuestionCreateRequest(String title, String question, Character type, String hint, String answer, String explanation, LocalDateTime generatedDate, LocalDateTime targetDate, List<String> candidates, List<String> students) {
        this.title = title;
        this.question = question;
        this.type = type;
        this.hint = hint;
        this.answer = answer;
        this.explanation = explanation;
        this.generatedDate = generatedDate;
        this.targetDate = targetDate;
        this.candidates = candidates;
        this.students = students;
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

    public LocalDateTime getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDateTime targetDate) {
        this.targetDate = targetDate;
    }

    public List<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<String> candidates) {
        this.candidates = candidates;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "QuestionCreateRequest{" +
                "title='" + title + '\'' +
                ", question='" + question + '\'' +
                ", type=" + type +
                ", hint='" + hint + '\'' +
                ", answer='" + answer + '\'' +
                ", explanation='" + explanation + '\'' +
                ", generatedDate=" + generatedDate +
                ", targetDate=" + targetDate +
                ", candidates=" + candidates +
                ", students=" + students +
                '}';
    }
}
