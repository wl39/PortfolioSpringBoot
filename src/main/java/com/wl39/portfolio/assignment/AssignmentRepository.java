package com.wl39.portfolio.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    @Query("SELECT a FROM Assignment a WHERE YEAR(a.targetDate) = :year AND MONTH(a.targetDate) = :month AND a.id.name = :name")
    List<Assignment> getAssignmentsByNameAndYearMonth(@Param("name") String name, @Param("year") int year, @Param("month") int month);
}
