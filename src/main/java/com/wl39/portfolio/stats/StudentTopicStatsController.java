package com.wl39.portfolio.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
