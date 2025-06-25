package com.wl39.portfolio.calendar;

import com.wl39.portfolio.user.CustomUserPrincipal;
import com.wl39.portfolio.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/calendars")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;
    private final UserService userService;

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
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        boolean isParent = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PARENT"));

        if (isAdmin) {
            return ResponseEntity.ok(this.calendarService.findByYearMonthAndStudent(year, month, name));
        }

        if (user.getUsername().equalsIgnoreCase(name)) {
            return ResponseEntity.ok(this.calendarService.findByYearMonthAndStudent(year, month, name));
        }

        if (isParent) {
            boolean isChild = userService.isMyChild(user.getUsername(), name); // 구현 필요
            if (isChild) {
                return ResponseEntity.ok(this.calendarService.findByYearMonthAndStudent(year, month, name));
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }

    @PatchMapping("/{name}")
    public ResponseEntity<?> reloadCalendars(@PathVariable String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return ResponseEntity.ok(this.calendarService.reloadCalendars(name));
    }
}