package com.wl39.portfolio.submission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    @Query("SELECT DISTINCT s FROM Submission s JOIN FETCH s.question q WHERE s.studentName = :studentName")
    Page<Object[]> findByStudentName(@Param("studentName") String studentName, Pageable pageable);
}
