package com.wl39.portfolio.simple_submission;

import jakarta.persistence.*;

@Entity
@Table(name = "simple_submission")
public class SimpleSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String question;
    private String answer;

}
