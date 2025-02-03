package com.wl39.portfolio.assignment;

import com.wl39.portfolio.calendar.CalendarService;
import com.wl39.portfolio.question.Question;
import com.wl39.portfolio.question.QuestionRepository;
import com.wl39.portfolio.question.QuestionService;
import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final QuestionService questionService;
    private final StudentService studentService;
    private final CalendarService calendarService;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, QuestionService questionService, StudentService studentService, CalendarService calendarService) {
        this.assignmentRepository = assignmentRepository;
        this.questionService = questionService;
        this.studentService = studentService;
        this.calendarService = calendarService;
    }

    @Transactional
    public void assignQuestions(AssignmentsDTO assignmentsDTO) {
        List<Assignment> assignments = new ArrayList<>();


        for (String name : assignmentsDTO.getNames()) {
            Student student = studentService.get(name);

            int ms = 0;
            int ss = 0;

            LocalDate date = assignmentsDTO.getTargetDate().toLocalDate();

            for (Long id : assignmentsDTO.getQuestionIds()) {
                Question question = questionService.get(id);

                assignments.add(new Assignment(question, student, assignmentsDTO.getTargetDate()));

                if (question.getType() == 'm') {
                    ms++;
                } else {
                    ss++;
                }
            }

            calendarService.submitAnswers(student, date, ms, ss);
        }

        assignmentRepository.saveAll(assignments);
    }

    public Assignment assignQuestion(Question question, Student student, LocalDateTime targetDate) {
        Assignment assignment = new Assignment();

        assignment.setStudent(student);
        assignment.setQuestion(question);
        assignment.setTargetDate(targetDate);

        return assignmentRepository.save(assignment);
    }
}
