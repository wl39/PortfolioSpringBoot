package com.wl39.portfolio.calendar;

import com.wl39.portfolio.user.CustomUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/{name}/{year}/{month}")
    public ResponseEntity<?> getCalendars(@PathVariable int year, @PathVariable int month, @PathVariable String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !user.getUsername().equals(name)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return ResponseEntity.ok(this.calendarService.findByYearMonthAndStudent(year, month, name));
    }
}