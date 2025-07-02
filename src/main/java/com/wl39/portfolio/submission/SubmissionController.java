package com.wl39.portfolio.submission;

import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.user.CustomUserPrincipal;
import com.wl39.portfolio.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/submissions")
@RequiredArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;
    private final UserService userService;


    @PostMapping("multiples")
    public void postSubmission(@RequestBody List<SubmissionCreateRequest> submissionCreateRequest) {
        this.submissionService.postSubmission(submissionCreateRequest);
    }

    @GetMapping("/mark/{name}")
    public Page<SubmissionQuestion> getSubmissionsToMark(Pageable pageable, @PathVariable String name) {
        return submissionService.getSubmissionsToMark(pageable, name);
    }

    @GetMapping("/review/{name}")
    public Page<SubmissionQuestion> getWrongSubmissions(Pageable pageable, @PathVariable String name) {
        return submissionService.getWrongSubmissions(pageable, name);
    }

    @PutMapping("/mark/{name}")
    public void markSubmission(@RequestBody Map<Long, Integer> marks, @PathVariable String name) {
        this.submissionService.putSubmission(marks, name);
    }

    @GetMapping("/day_counts")
    public ResponseEntity<?> getAllSubmissionDayCountsByName(@RequestParam String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();


        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        boolean isParent = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PARENT"));

        if (isAdmin) {
            return submissionService.getAllSubmissionDayCountsByName(name);
        }

        if (user.getUsername().equals(name)) {
            return submissionService.getAllSubmissionDayCountsByName(name);
        }

        if (isParent) {
            boolean isChild = userService.isMyChild(user.getUsername(), name); // 구현 필요
            if (isChild) {
                return submissionService.getAllSubmissionDayCountsByName(name);
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }

    @GetMapping("/day_counts/latest")
    public ResponseEntity<?> getLatestSubmissionDayCountsAutoByName(
            @RequestParam String name,
            Authentication authentication) {

        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        boolean isParent = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PARENT"));

        if (isAdmin) {
            return submissionService.getLatestSubmissionDayCountsByName(name);
        }

        if (user.getUsername().equals(name)) {
            return submissionService.getLatestSubmissionDayCountsByName(name);
        }

        if (isParent) {
            boolean isChild = userService.isMyChild(user.getUsername(), name); // 구현 필요
            if (isChild) {
                return submissionService.getLatestSubmissionDayCountsByName(name);
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }

    @GetMapping("/count/{name}")
    public ResponseEntity<?> getTotalCountsByName(
            @PathVariable String name,
            Authentication authentication) {

        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        boolean isParent = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PARENT"));

        if (isAdmin) {
            return ResponseEntity.ok(submissionService.getTotalCountsByName(name));
        }

        if (user.getUsername().equals(name)) {
            return ResponseEntity.ok(submissionService.getTotalCountsByName(name));
        }

        if (isParent) {
            boolean isChild = userService.isMyChild(user.getUsername(), name); // 구현 필요
            if (isChild) {
                return ResponseEntity.ok(submissionService.getTotalCountsByName(name));
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }


    @GetMapping("/{name}")
    public Page<SubmissionQuestion> getSubmissions(Pageable pageable, @PathVariable String name, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return submissionService.getSubmissions(pageable, name, date);
    }
}
