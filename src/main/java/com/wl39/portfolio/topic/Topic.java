package com.wl39.portfolio.topic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wl39.portfolio.calendar.Calendar;
import com.wl39.portfolio.question.Question;
import com.wl39.portfolio.stats.StudentTopicStats;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topic")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;

    @ManyToMany(mappedBy = "topics")
    @JsonIgnore
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<StudentTopicStats> studentTopicStats = new HashSet<>();

}
