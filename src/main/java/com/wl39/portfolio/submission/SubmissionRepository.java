package com.wl39.portfolio.submission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wl39.portfolio.simple_submission.SimpleSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


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

    @Query("""
            SELECT new com.wl39.portfolio.submission.SubmissionDayCount(
            stu.name, SUM(CASE WHEN s.marked = -1 THEN 1 ELSE 0 END), SUM(CASE WHEN s.marked <> -1 THEN 1 ELSE 0 END), FUNCTION('date', s.submitDate))
            FROM Submission s
            JOIN s.student stu
            WHERE stu.name = :name
            GROUP BY FUNCTION('date', s.submitDate)
            """)
    List<SubmissionDayCount> getAllSubmissionDayCountsByName(@Param("name") String name);

    @Query("""
            SELECT new com.wl39.portfolio.submission.SubmissionDayCount(
            stu.name, SUM(CASE WHEN s.marked = -1 THEN 1 ELSE 0 END), SUM(CASE WHEN s.marked <> -1 THEN 1 ELSE 0 END), FUNCTION('date', s.submitDate))
            FROM Submission s
            JOIN s.student stu
            WHERE stu.name = :name AND FUNCTION('date', s.submitDate) = :date
            """)
    SubmissionDayCount getLatestSubmissionDayCountsByName(@Param("name") String name, @Param("date") LocalDate date);

    @Query("""
        SELECT FUNCTION('date', MAX(s.submitDate))
        FROM Submission s
        JOIN s.student stu
        WHERE stu.name = :name
    """)
    Optional<LocalDate> findLatestSubmitDateByName(@Param("name") String name);


}
