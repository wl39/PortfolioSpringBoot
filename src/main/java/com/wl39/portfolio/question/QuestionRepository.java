package com.wl39.portfolio.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("""
                SELECT q
                FROM Question q
                WHERE EXISTS (
                    SELECT 1
                    FROM Assignment a
                    WHERE a.question = q
                      AND a.student.name = :studentName
                )
            """)
    Page<Question> findByStudentNameFetchAssignments(Pageable pageable, @Param("studentName") String studentName);
    @Query("""
                SELECT q
                FROM Question q
                WHERE EXISTS (
                    SELECT 1
                    FROM Assignment a
                    WHERE a.question = q
                      AND a.student.name = :studentName
                      AND FUNCTION('YEAR', a.targetDate) = :year
                      AND FUNCTION('MONTH', a.targetDate) = :month
                      AND FUNCTION('DAY', a.targetDate) = :day
                )
            """)
    Page<Question> findByStudentNameFetchAssignments(Pageable pageable, @Param("studentName") String studentName,
                                                     @Param("year") int year,
                                                     @Param("month") int month,
                                                     @Param("day") int day);

    Page<Question> findByAssignments_Student_Name(Pageable pageable, String name);

    Page<Question> findByTitle(Pageable pageable, String title);
}
