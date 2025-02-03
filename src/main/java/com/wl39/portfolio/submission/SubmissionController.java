package com.wl39.portfolio.submission;

import com.wl39.portfolio.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping("multiples")
    public void postSubmission(@RequestBody List<SubmissionCreateRequest> submissionCreateRequest) {
        this.submissionService.postSubmission(submissionCreateRequest);
    }

    @GetMapping("/{name}")
    public Page<SubmissionQuestion> getSubmissions(Pageable pageable, @PathVariable String name) {
        return submissionService.getSubmissions(pageable, name);
    }

    @GetMapping("/mark/{name}")
    public Page<SubmissionQuestion> getSubmissionsToMark(Pageable pageable, @PathVariable String name) {
        return submissionService.getSubmissionsToMark(pageable, name);
    }

    @GetMapping("/review/{name}")
    public Page<SubmissionQuestion> getWrongSubmissions(Pageable pageable, @PathVariable String name) {
        return submissionService.getWrongSubmissions(pageable, name);
    }

    @PutMapping("/mark")
    public void markSubmission(@RequestBody Map<Long, Integer> marks) {
        this.submissionService.putSubmission(marks);
    }
}
