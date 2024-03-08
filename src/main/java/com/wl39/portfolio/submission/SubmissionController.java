package com.wl39.portfolio.submission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping
    public Page<Object[]> findByStudentName(@RequestParam String studentName, Pageable pageable) {
        return this.submissionService.findByStudentName(studentName, pageable);
    }

    @PostMapping
    public Long addSubmission(@RequestParam Long questionId, @RequestParam String studentAnswer, @RequestParam String studentName) {
        System.out.println(questionId + " " + studentAnswer + " " + studentName);
        return submissionService.addSubmission(questionId, studentAnswer, studentName);
    }

    @PostMapping("/multiples")
    public void addSubmissions(@RequestBody List<SubmissionDTO> submissions) {
        submissionService.addSubmissions(submissions);
    }

}
