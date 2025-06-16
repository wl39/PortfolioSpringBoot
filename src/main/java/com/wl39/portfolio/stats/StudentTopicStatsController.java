package com.wl39.portfolio.stats;

import com.wl39.portfolio.user.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/stats/topics")
@RequiredArgsConstructor
public class StudentTopicStatsController {
    private final StudentTopicStatsService studentTopicStatsService;

    @GetMapping(path = "/{name}")
    public ResponseEntity<?> getAll(@PathVariable String name) {
        return ResponseEntity.ok(studentTopicStatsService.getAll(name));
    }

    @GetMapping(path ="/page/{name}")
    public ResponseEntity<?> getAll(Pageable pageable, @PathVariable String name) {
        return ResponseEntity.ok(studentTopicStatsService.getAll(pageable, name));
    }

    @PatchMapping(path = "/reload/{name}")
    public ResponseEntity<?> reload(@PathVariable String name, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return ResponseEntity.ok(studentTopicStatsService.reloadStatsForStudent(name));
    }
}
