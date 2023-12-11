package com.wl39.portfolio.stopwatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
//    public List<Stopwatch> getAllStopwatches() {
//        return this.stopwatchService.getAllStopwatches();
//    }
    public Page<Stopwatch> getAllStopwatchesPage(Pageable pageable) {
        return this.stopwatchService.getStopwatchesPage(pageable);
    }
    @PostMapping
    public Long uploadStopwatch(@RequestBody Stopwatch stopwatch) {
        this.stopwatchService.uploadStopwatch(stopwatch);

        return stopwatch.getId();
    }

}
