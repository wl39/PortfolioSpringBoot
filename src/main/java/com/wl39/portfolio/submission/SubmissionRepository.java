package com.wl39.portfolio.submission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Page<Submission> findByStudentName(Pageable pageable, String name);
    Page<Submission> findByStudentNameAndMarked(Pageable pageable, String name, Integer marked);

    @Query("""
            SELECT COUNT(DISTINCT s)
            FROM Submission s
            JOIN s.student stu
            WHERE YEAR(s.targetDate) = :year AND MONTH(s.targetDate) = :month
            AND stu.name = :name
            """) // WHERE MARKED -1 OR 1 OR 0
    Long count(@Param("name") String name, @Param("year") int year, @Param("month") int month);

    @Query("""
            SELECT COUNT(DISTINCT s)
            FROM Submission s
            JOIN s.student stu
            WHERE YEAR(s.targetDate) = :year AND MONTH(s.targetDate) = :month
            AND stu.name = :name
            AND s.marked = -1
            """) // WHERE MARKED -1 OR 1 OR 0
    Long unmarked(@Param("name") String name, @Param("year") int year, @Param("month") int month);
}
