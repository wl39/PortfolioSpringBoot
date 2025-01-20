package com.wl39.portfolio.simple_submission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SimpleSubmissionRepository extends JpaRepository<SimpleSubmission, Long> {
    long countByName(String name);

    @Query("SELECT COUNT(s) FROM SimpleSubmission s WHERE s.name = :name AND DATE(s.submitDate) = :date")
    long countByNameAndSubmitDate(@Param("name") String name, @Param("date") LocalDate submitDate);
}
