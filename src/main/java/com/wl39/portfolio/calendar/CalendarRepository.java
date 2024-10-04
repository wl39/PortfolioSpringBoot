package com.wl39.portfolio.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, CalendarID> {
    @Modifying
    @Transactional
    @Query("UPDATE Calendar c " +
            "SET c.solved = CASE " +
            "WHEN c.solved + 1 + c.unmarked > c.questions THEN c.questions " +
            "ELSE c.solved + 1 END " +
            "WHERE c.id.date = :date AND c.id.studentName = :name")
    int solved(@Param("date") LocalDate date, @Param("name") String name);

    @Modifying
    @Transactional
    @Query("UPDATE Calendar c " +
            "SET c.unmarked = CASE " +
            "WHEN c.unmarked + 1 + c.solved > c.questions THEN c.questions " +
            "ELSE c.unmarked + 1 END " +
            "WHERE c.id.date = :date AND c.id.studentName = :name")
    int unmarked(@Param("date") LocalDate date, @Param("name") String name);

    @Modifying
    @Transactional
    @Query("UPDATE Calendar c SET c.questions = c.questions + 1 WHERE c.id.date = :date AND c.id.studentName = :name")
    int questions(@Param("date") LocalDate date, @Param("name") String name);

    @Query("SELECT c FROM Calendar c WHERE YEAR(c.id.date) = :year AND MONTH(c.id.date) = :month")
    List<Calendar> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT c FROM Calendar c WHERE YEAR(c.id.date) = :year AND MONTH(c.id.date) = :month AND c.id.studentName = :name")
    List<Calendar> findByYearMonthAndStudent(@Param("year") int year, @Param("month") int month, String name);
}
