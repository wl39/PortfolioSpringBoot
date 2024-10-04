package com.wl39.portfolio.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public CalendarID uploadCalendar(Calendar calendar) {
        this.calendarRepository.save(calendar);

        return calendar.getCalendarID();
    }

    public CalendarID uploadCalendar(CalendarID id , int questions) {
        Calendar calendar = new Calendar(id, questions, 0, 0);
        this.calendarRepository.save(calendar);

        return id;
    }

    public CalendarID uploadCalendar(LocalDate date, String name, int questions) {
        CalendarID id = new CalendarID(date, name);

        Calendar calendar = new Calendar(id, questions, 0, 0);
        this.calendarRepository.save(calendar);

        return id;
    }

    public int solved(LocalDate date, String name) {
        return this.calendarRepository.solved(date, name);
    }

    public int questions(LocalDate date, String name) {
        return this.calendarRepository.questions(date, name);
    }

    public boolean findById(LocalDate date, String name) {
        Optional<Calendar> calendar = this.calendarRepository.findById(new CalendarID(date, name));
        return calendar.isPresent();
    }

    public List<Calendar> findByYearAndMonth(int year, int month) {
        return this.calendarRepository.findByYearAndMonth(year, month);
    }

    public List<Calendar> findByYearMonthAndStudent(int year, int month, String name) {
        return this.calendarRepository.findByYearMonthAndStudent(year, month, name);
    }
}
