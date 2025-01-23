package com.wl39.portfolio.assignment;

import com.wl39.portfolio.question.QuestionForStudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:3000")
@RequestMapping(path = "api/v1/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public List<Assignment> getAssignmentsByNameAndYearMonth(@RequestParam String name, @RequestParam int year, @RequestParam int month) {
        return assignmentService.getAssignmentsByNameAndYearMonth(name, year, month);
    }

    @PostMapping
    public List<Long> assignQuestions(@RequestBody AssignmentDTO assignments) {
        return this.assignmentService.assignQuestions(assignments);
    }
}
