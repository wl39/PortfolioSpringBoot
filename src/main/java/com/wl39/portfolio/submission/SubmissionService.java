package com.wl39.portfolio.submission;

import com.wl39.portfolio.PostTransactionTaskScheduler;
import com.wl39.portfolio.assignment.Assignment;
import com.wl39.portfolio.assignment.AssignmentRepository;
import com.wl39.portfolio.calendar.CalendarService;
import com.wl39.portfolio.question.Question;
import com.wl39.portfolio.question.QuestionRepository;
import com.wl39.portfolio.stats.StudentTopicStatsService;
import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.student.StudentRepository;
import com.wl39.portfolio.topic.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;
    private final QuestionRepository questionRepository;
    private final AssignmentRepository assignmentRepository;
    private final CalendarService calendarService;
    private final PostTransactionTaskScheduler postTransactionTaskScheduler;
    private final StudentTopicStatsService studentTopicStatsService;

    @Transactional
    public void postSubmission(List<SubmissionCreateRequest> submissionCreateRequest) {
        int MCQCounts = 0;
        int SAQCounts = 0;

        List<SubmissionTopic> submissionTopics = new ArrayList<>();

        Student student = new Student();

        for (SubmissionCreateRequest scr : submissionCreateRequest) {
            Submission submission = new Submission();

            Assignment assignment = new Assignment();
            Question question = new Question();

            question = questionRepository.findById(scr.getQuestionId()).orElseThrow();
            student = studentRepository.findByName(scr.getStudentName()).orElseThrow();

            SubmissionTopic submissionTopic = new SubmissionTopic();
            submissionTopic.setQuestionId(question.getId());
            submissionTopic.setTopics(question.getTopics().stream()
                    .map(Topic::getTitle)
                    .collect(Collectors.toSet()));

            if (question.getType() == 's') {
                SAQCounts++;
                submission.setMarked(0);
            } else {
                MCQCounts++;
                if (!scr.getStudentAnswer().equals(question.getAnswer())) {
                    submission.setMarked(-1);
                    submissionTopic.setMarked(-1);
                } else {
                    submission.setMarked(1);
                    submissionTopic.setMarked(1);
                }
            }

            submission.setStudent(student);
            submission.setQuestion(question);
            submission.setStudentAnswer(scr.getStudentAnswer());

            assignment = assignmentRepository.findByQuestion_IdAndStudent_Name(scr.getQuestionId(), scr.getStudentName()).orElseThrow();

            submission.setTargetDate(assignment.getTargetDate());

            LocalDateTime submitDate = LocalDateTime.now();

            submission.setSubmitDate(submitDate);
            submissionTopic.setSubmitDate(submitDate);

            assignmentRepository.deleteById(assignment.getId());

            submissionRepository.save(submission);

            // TODO: If the calendar is missing, make a new calendar and save it!
            calendarService.submitAnswer(student, assignment.getTargetDate().toLocalDate(), question.getType() == 's');

            submissionTopics.add(submissionTopic);
        }

        Student finalStudent = student;

        postTransactionTaskScheduler.runAfterCommit(() -> {
            // 원하는 작업 실행
            System.out.println("Student's stat recalculated: Student: " + finalStudent.getName());
            studentTopicStatsService.updateStatsForStudent(finalStudent, submissionTopics);
        }, 1000);
    }

    public Page<SubmissionQuestion> getSubmissions(Pageable pageable, String name, LocalDate date) {
        if (date != null) {
            return submissionRepository.findByStudentNameAndDate(pageable, name, date.getYear(), date.getMonthValue(), date.getDayOfMonth()).map(submission ->
                    new SubmissionQuestion(submission.getId(), submission.getQuestion(), submission.getStudentAnswer(), submission.getSubmitDate(), submission.getTargetDate(), submission.getMarked())
            );
        }

        return submissionRepository.findByStudentName(pageable, name).map(submission ->
                new SubmissionQuestion(submission.getId(), submission.getQuestion(), submission.getStudentAnswer(), submission.getSubmitDate(), submission.getTargetDate(), submission.getMarked())
        );
    }

    public Page<SubmissionQuestion> getSubmissionsToMark(Pageable pageable, String name) {
        return submissionRepository.findByStudentNameAndMarked(pageable, name, 0).map(submission ->
                new SubmissionQuestion(submission.getId(), submission.getQuestion(), submission.getStudentAnswer(), submission.getSubmitDate(), submission.getTargetDate(), submission.getMarked())
        );
    }

    public Page<SubmissionQuestion> getWrongSubmissions(Pageable pageable, String name) {
        return this.submissionRepository.findByStudentNameAndMarked(pageable, name, -1).map(submission ->
                new SubmissionQuestion(submission.getId(), submission.getQuestion(), submission.getStudentAnswer(), submission.getSubmitDate(), submission.getTargetDate(), submission.getMarked())
        );
    }

    public void putSubmission(Map<Long, Integer> marks, String name) {
        for (Map.Entry<Long, Integer> entry : marks.entrySet()) {
            Long id = entry.getKey();
            Integer mark = entry.getValue();

            Submission submission = this.submissionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "submission not found"));

            submission.setMarked(mark);

            Student student = this.studentRepository.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found"));
            this.calendarService.markSAQQuestion(submission.getTargetDate().getYear(), submission.getTargetDate().getMonthValue(), submission.getTargetDate().getDayOfMonth(), name);

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

    public List<Submission> getAllSubmissionsByName(String name) {
        return submissionRepository.findByStudentName(name);
    }

    public Long getTotalCountsByName(String name) {
        return submissionRepository.countByStudentName(name);
    }
}
