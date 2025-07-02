package com.wl39.portfolio.assignment;

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

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByQuestion_IdAndStudent_Name(Long questionId, String name);

    @Query("""
            SELECT COUNT(DISTINCT a)
            FROM Assignment a
            JOIN a.student s
            WHERE YEAR(a.targetDate) = :year AND MONTH(a.targetDate) = :month
            AND s.name = :name
            """) // WHERE MARKED -1 OR 1 OR 0
    Long getCounts(@Param("name") String name, @Param("year") int year, @Param("month") int month);
    Long countByStudentName(String name);
    List<Assignment> findByStudentName(String name);

    Optional<Assignment> findTopByStudent_NameOrderByTargetDateDesc(String name);

    @Query("""
    SELECT a
    FROM Assignment a
    JOIN a.student s
    WHERE FUNCTION('YEAR', a.targetDate) = :year
      AND FUNCTION('MONTH', a.targetDate) = :month
      AND FUNCTION('DAY', a.targetDate) = :day
      AND s.name = :name
    """)
    Page<Assignment> findByStudentNameAndTargetDate(
            @Param("name") String name,
            @Param("year") int year,
            @Param("month") int month,
            @Param("day") int day,
            Pageable pageable
    );

    @Query("""
            SELECT a.question.id
            FROM Assignment a
            JOIN a.student s
            WHERE s.name = :name
            """)
    List<Long> findByQuestionIdsByStudentName(@Param("name") String name);

//    @Query("""
//    SELECT a
//    FROM Assignment a
//    JOIN a.student s
//    WHERE FUNCTION('YEAR', a.targetDate) = :year
//      AND FUNCTION('MONTH', a.targetDate) = :month
//      AND FUNCTION('DAY', a.targetDate) = :day
//      AND s.name = :name
//    """)
//    Page<Assignment> findByStudentNameAndTargetDateAndTitleAndQuestion(
//            @Param("name") String name,
//            @Param("year") int year,
//            @Param("month") int month,
//            @Param("day") int day,
//            Pageable pageable
//    );
}
