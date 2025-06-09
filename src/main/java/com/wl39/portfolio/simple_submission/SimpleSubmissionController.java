package com.wl39.portfolio.simple_submission;

import com.wl39.portfolio.user.CustomUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "api/v1/simple_math")
public class SimpleSubmissionController {
    private final SimpleSubmissionService simpleSubmissionService;

    public SimpleSubmissionController(SimpleSubmissionService simpleSubmissionService) {
        this.simpleSubmissionService = simpleSubmissionService;
    }

    @GetMapping
    public Page<SimpleSubmission> getAllSimpleSubmissionPages(Pageable pageable) {
        return simpleSubmissionService.getSubmissions(pageable);
    }

    @PostMapping
    public Long uploadSimpleSubmission(@RequestBody @Valid SimpleSubmission simpleSubmission) {
        this.simpleSubmissionService.uploadSubmission(simpleSubmission);

        return simpleSubmission.getId();
    }

    @GetMapping("/count")
    public Long getCounts(@RequestParam String name) {
        return this.simpleSubmissionService.getCounts(name);
    }

    @GetMapping("/day_count")
    public Long getCounts(@RequestParam String name, @RequestParam LocalDate submitDate) {
        return this.simpleSubmissionService.getCounts(name, submitDate);
    }

    @GetMapping("/count/wrong")
    public Long getWrongCounts(@RequestParam String name) {
        return this.simpleSubmissionService.getWrongCounts(name);
    }

    @GetMapping("/count/right")
    public Long getRightCounts(@RequestParam String name) {
        return this.simpleSubmissionService.getRightCounts(name);
    }

    @GetMapping("/day_count/wrong")
    public Long getWrongCounts(@RequestParam String name, @RequestParam LocalDate submitDate) {
        return this.simpleSubmissionService.getWrongCounts(name, submitDate);
    }

    @GetMapping("/day_ count/right")
    public Long getRightCounts(@RequestParam String name, @RequestParam LocalDate submitDate) {
        return this.simpleSubmissionService.getRightCounts(name, submitDate);
    }

    @GetMapping("/day_counts/page")
    public ResponseEntity<?> getDayCountsByName(Pageable pageable, @RequestParam String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !user.getUsername().equals(name)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return ResponseEntity.ok(this.simpleSubmissionService.getDayCountsByName(pageable, name));
    }

    @GetMapping("/day_counts/latest")
    public ResponseEntity<?> getLatestSubmissionDayCountsAutoByName(@RequestParam String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !user.getUsername().equals(name)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return this.simpleSubmissionService.getLatestSubmissionDayCountsByName(name);
    }
}
