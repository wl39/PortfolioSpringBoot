package com.wl39.portfolio.submission;

import com.wl39.portfolio.assignment.Assignment;
import com.wl39.portfolio.assignment.AssignmentRepository;
import com.wl39.portfolio.calendar.CalendarService;
import com.wl39.portfolio.question.Question;
import com.wl39.portfolio.question.QuestionRepository;
import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final AssignmentRepository assignmentRepository;
    private final CalendarService calendarService;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository, StudentRepository studentRepository, QuestionRepository questionRepository, AssignmentRepository assignmentRepository, CalendarService calendarService) {
        this.submissionRepository = submissionRepository;
        this.studentRepository = studentRepository;
        this.questionRepository = questionRepository;
        this.assignmentRepository = assignmentRepository;
        this.calendarService = calendarService;
    }

    @Transactional
    public void postSubmission(List<SubmissionCreateRequest> submissionCreateRequest) {
        int MCQCounts = 0;
        int SAQCounts = 0;

        for (SubmissionCreateRequest scr : submissionCreateRequest) {
            Submission submission = new Submission();

            Assignment assignment = new Assignment();
            Question question = new Question();
            Student student = new Student();

            question = questionRepository.findById(scr.getQuestionId()).orElseThrow();
            student = studentRepository.findByName(scr.getStudentName()).orElseThrow();

            if (question.getType() == 's') {
                SAQCounts++;
                submission.setMarked(0);
            } else {
                MCQCounts++;
                if (!scr.getStudentAnswer().equals(question.getAnswer())) {
                    submission.setMarked(-1);
                } else {
                    submission.setMarked(1);
                }
            }

            submission.setStudent(student);
            submission.setQuestion(question);
            submission.setStudentAnswer(scr.getStudentAnswer());

            assignment = assignmentRepository.findByQuestion_IdAndStudent_Name(scr.getQuestionId(), scr.getStudentName()).orElseThrow();

            submission.setTargetDate(assignment.getTargetDate());
            submission.setSubmitDate(LocalDateTime.now());

            assignmentRepository.deleteById(assignment.getId());

            submissionRepository.save(submission);

            calendarService.submitAnswer(student, assignment.getTargetDate().toLocalDate(), question.getType() == 's');
        }
    }

    public Page<SubmissionQuestion> getSubmissions(Pageable pageable, String name) {
        return submissionRepository.findByStudentName(pageable, name).map(submission ->
             new SubmissionQuestion(submission.getId(), submission.getQuestion(), submission.getStudentAnswer(), submission.getSubmitDate(), submission.getMarked())
        );
    }

    public Page<SubmissionQuestion> getSubmissionsToMark(Pageable pageable, String name) {
        return submissionRepository.findByStudentNameAndMarked(pageable, name, 0).map(submission ->
                new SubmissionQuestion(submission.getId(), submission.getQuestion(), submission.getStudentAnswer(), submission.getSubmitDate(), submission.getMarked())
        );
    }

    public Page<SubmissionQuestion> getWrongSubmissions(Pageable pageable, String name) {
        return this.submissionRepository.findByStudentNameAndMarked(pageable, name, -1).map(submission ->
             new SubmissionQuestion(submission.getId(), submission.getQuestion(), submission.getStudentAnswer(), submission.getSubmitDate(), submission.getMarked())
        );
    }

    public void putSubmission(Map<Long, Integer> marks) {
        for (Map.Entry<Long, Integer> entry : marks.entrySet()) {
            Long id = entry.getKey();
            Integer mark = entry.getValue();

            Submission submission = this.submissionRepository.findById(id).orElseThrow();

            submission.setMarked(mark);

            this.submissionRepository.save(submission);
        }
    }

    public ResponseEntity<?> getAllSubmissionDayCountsByName(String name) {
        return ResponseEntity.ok(submissionRepository.getAllSubmissionDayCountsByName(name));
    }

    public ResponseEntity<?> getLatestSubmissionDayCountsByName(String name) {
        LocalDate date = submissionRepository.findLatestSubmitDateByName(name).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Either date or student not found")
        );

        return ResponseEntity.ok(submissionRepository.getLatestSubmissionDayCountsByName(name, date));
    }
}
