package com.wl39.portfolio.calendar;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByStudent_NameAndYearAndMonth(String name, Integer year, Integer month);
    Optional<Calendar> findByStudent_NameAndYearAndMonthAndDay(String name, Integer year, Integer month, Integer day);
    List<Calendar> findByYearAndMonthAndStudent_Name(Integer year, Integer month, String name);
}
