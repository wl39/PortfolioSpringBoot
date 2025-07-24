package com.wl39.portfolio.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping(path = "/{name}/students")
    public ResponseEntity<?> getAllStudents(@PathVariable String name) {
        return ResponseEntity.ok(teacherService.getAllStudents(name));
    }
}
