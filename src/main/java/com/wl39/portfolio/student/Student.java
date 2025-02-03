package com.wl39.portfolio.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wl39.portfolio.assignment.Assignment;
import com.wl39.portfolio.calendar.Calendar;
import com.wl39.portfolio.submission.Submission;
import jakarta.persistence.*;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;  // 학생 이름
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Assignment> assignments = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Submission> submissions = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Calendar> calendars = new HashSet<>();

    public Student() {}

    public Student(String name) {
        this.name = name;
    }

    // Getter/Setter
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Assignment> getAssignments() {
        return assignments;
    }
    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Set<Submission> getSubmissions() {
        return submissions;
    }
    public void setSubmissions(Set<Submission> submissions) {
        this.submissions = submissions;
    }

    // 편의 메서드 (양방향 연결 시 종종 사용)
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
        assignment.setStudent(this);
    }

    public void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
        assignment.setStudent(null);
    }

    public void addSubmission(Submission submission) {
        submissions.add(submission);
        submission.setStudent(this);
    }

    public void removeSubmission(Submission submission) {
        submissions.remove(submission);
        submission.setStudent(null);
    }

    public Set<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(Set<Calendar> calendars) {
        this.calendars = calendars;
    }
}
