package com.wl39.portfolio.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public void assignQuestions(@RequestBody AssignmentsDTO assignmentsDTO) {
        assignmentService.assignQuestions(assignmentsDTO);
    }
}
