package com.wl39.portfolio.stopwatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:3000")
@RequestMapping(path = "api/v1/stopwatch")
public class StopwatchController {
    private final StopwatchService stopwatchService;

    @Autowired
    public StopwatchController(StopwatchService stopwatchService) {
        this.stopwatchService = stopwatchService;
    }

    @GetMapping
    public List<Stopwatch> getAllStopwatches() {
        return this.stopwatchService.getAllStopwatches();
    }

    @PostMapping
    public Long uploadStopwatch(@RequestBody Stopwatch stopwatch) {
        this.stopwatchService.uploadStopwatch(stopwatch);

        return stopwatch.getId();
    }

}
