package com.wl39.portfolio.simple_submission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SimpleSubmissionRepository extends JpaRepository<SimpleSubmission, Long> {
    long countByName(String name);
}
