package com.wl39.portfolio.student;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByName(String name);

    @Query("""
            SELECT DISTINCT s
            FROM Student s
            JOIN FETCH s.assignments a
            JOIN FETCH s.submissions sub
            WHERE YEAR(a.targetDate) = :year AND MONTH(a.targetDate) = :month
            AND YEAR(sub.targetDate) = :year AND MONTH(sub.targetDate) = :month
            AND s.name = :name
            """)
    List<Student> findAssignmentsAndSubmissionsByName(String name, int year, int month);
    boolean existsByName(String name);
}
