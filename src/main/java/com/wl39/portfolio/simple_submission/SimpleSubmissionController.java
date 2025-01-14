package com.wl39.portfolio.simple_submission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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
    public Long uploadSimpleSubmission(@RequestBody SimpleSubmission simpleSubmission) {
        this.simpleSubmissionService.uploadSubmission(simpleSubmission);

        return simpleSubmission.getId();
    }

    @GetMapping("/count")
    public Long getCounts(@RequestParam String name) {
        return this.simpleSubmissionService.getCounts(name);
    }
}
