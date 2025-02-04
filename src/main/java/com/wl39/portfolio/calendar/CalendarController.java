package com.wl39.portfolio.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/calendars")
public class CalendarController {
    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/{year}/{month}")
    public List<Calendar> getCalendars(@PathVariable int year, @PathVariable int month, @RequestParam List<String> students) {
        List<Calendar> list = new ArrayList<>();

        for (String name : students) {
            list.addAll(this.calendarService.findByYearMonthAndStudent(year, month, name));
        }

        return list;
    }
}