package com.wl39.portfolio.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wl39.portfolio.assignment.Assignment;
import com.wl39.portfolio.calendar.Calendar;
import com.wl39.portfolio.stats.StudentTopicStats;
import com.wl39.portfolio.submission.Submission;
import com.wl39.portfolio.subscription.StudentServiceSubscription;
import com.wl39.portfolio.teacher.Teacher;
import com.wl39.portfolio.user.UserEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;  // 학생 이름
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private UserEntity user;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Assignment> assignments = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Submission> submissions = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Calendar> calendars = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<StudentTopicStats> topicStats = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentServiceSubscription> subscriptions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonIgnore
    private Teacher teacher;

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

    public List<StudentServiceSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<StudentServiceSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Set<StudentTopicStats> getTopicStats() {
        return topicStats;
    }

    public void setTopicStats(Set<StudentTopicStats> topicStats) {
        this.topicStats = topicStats;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
