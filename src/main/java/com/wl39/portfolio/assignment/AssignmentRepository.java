package com.wl39.portfolio.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
    List<Assignment> findByStudentName(String name);

    Optional<Assignment> findTopByStudent_NameOrderByTargetDateDesc(String name);
}
