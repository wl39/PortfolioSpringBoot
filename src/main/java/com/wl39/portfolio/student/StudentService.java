package com.wl39.portfolio.student;

import com.wl39.portfolio.assignment.AssignmentRepository;
import com.wl39.portfolio.submission.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, AssignmentRepository assignmentRepository, SubmissionRepository submissionRepository) {
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
    }

    public List<Student> get(String name, int year, int month) {
        System.out.println(submissionRepository.count(name, year, month));
        System.out.println(submissionRepository.unmarked(name, year, month));
        System.out.println(assignmentRepository.getCounts(name, year, month));
        return this.studentRepository.findAssignmentsAndSubmissionsByName(name, year, month);
    }

    public Student get(String name) {
        return studentRepository.findByName(name).orElseGet(() -> {
            Student s = new Student(name);
            return studentRepository.save(s);
        });
    }
}
