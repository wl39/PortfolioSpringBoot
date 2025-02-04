package com.wl39.portfolio.student;

import com.wl39.portfolio.assignment.Assignment;
import com.wl39.portfolio.submission.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{year}/{month}")
    public List<StudentCalendar> get(@PathVariable int year, @PathVariable int month, @RequestParam List<String> students) {
        List<Student> studentList = new ArrayList<>();
        List<StudentCalendar> studentCalendars = new ArrayList<>();

        for (String name : students) {
            studentService.get(name, year, month);

//            studentList.addAll(this.studentService.get(name, year, month));
        }

//        for (Student student : studentList) {
//            for (Assignment assignment : student.getAssignments()) {
//
//            }
//
//            for (Submission submission : student.getSubmissions()) {
//
//            }
//        }


        return studentCalendars;
    }
}
