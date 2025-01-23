package com.wl39.portfolio.submission;

import com.wl39.portfolio.question.Question;
import com.wl39.portfolio.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository, QuestionRepository questionRepository) {
        this.submissionRepository = submissionRepository;
        this.questionRepository = questionRepository;
    }

    public void addSubmissions(List<SubmissionDTO> submissions) {
        for (SubmissionDTO submission : submissions) {
            Long questionId = submission.getQuestionId();
            String studentAnswer = submission.getStudentAnswer();
            String studentName = submission.getStudentName();
            LocalDateTime submitDate = submission.getSubmitDate();
            Boolean marked = submission.getMarked();

            Question question = this.questionRepository.findById(questionId).orElse(null);

            if (question != null) {
//                List<String> studentsFor = question.getStudentsFor();
//                if (studentAnswer != null && studentsFor.contains(studentName)) {
//                    studentsFor.remove(studentName);
//                    question.setStudentsFor(studentsFor);
//                    this.questionRepository.save(question);
//
//                    if(question.getType() == 's') { // s -> Short Answer Question
//                        this.calendarRepository.unmarked(question.getTargetDate().toLocalDate(), studentName);
//                    } else {
//                        this.calendarRepository.solved(question.getTargetDate().toLocalDate(), studentName);
//                    }
//                }
                Submission newSubmission = new Submission();
                newSubmission.setQuestion(question);
                newSubmission.setStudentAnswer(studentAnswer);
                newSubmission.setStudentName(studentName);
                newSubmission.setSubmitDate(submitDate);


                if (question.getType().equals('m')) {
                    newSubmission.setMarked(1);
                } else {
                    newSubmission.setMarked(0);
                }

                submissionRepository.save(newSubmission);
            }
        }
    }

    public Long addSubmission(Long questionId, String studentAnswer, String studentName) {
        Question question = this.questionRepository.findById(questionId).orElse(null);

        if (question != null) {
            Submission submission = new Submission();
            submission.setQuestion(question);
            submission.setStudentAnswer(studentAnswer);
            submission.setStudentName(studentName);
            submission.setSubmitDate(LocalDateTime.now());

            submissionRepository.save(submission);

            return submission.getId();
        }
        return -1L;
    }

    public Page<Object[]> findByStudentName(String studentName, Boolean getAll, Pageable pageable) {
        if (getAll)
            return this.submissionRepository.findByStudentName(studentName, pageable);
        else
            return this.submissionRepository.findMarkedByStudentName(studentName, pageable);
    }

    public Page<Object[]> findRevisionByStudentName(String studentName, Pageable pageable) {
        return this.submissionRepository.findRevisionsByStudentName(studentName, pageable);
    }

    public Page<Object[]> findMarkedByStudentName(String studentName, Pageable pageable) {
        return this.submissionRepository.findMarkedByStudentName(studentName, pageable);
    }


    public Page<Object[]> getSAQByStudentName(String studentName, Pageable pageable) {
        return this.submissionRepository.getSAQByStudentName(studentName, pageable);
    }

    public void markSAQ(String studentName, Map<Long, Integer> questionMarks) {
        for (Map.Entry<Long, Integer> entry : questionMarks.entrySet()) {
            Long questionId = entry.getKey();
            Integer mark = entry.getValue();
            Question question = this.questionRepository.findById(questionId).orElse(null);

            System.out.println(question);

            // Logic to update submission marks
            submissionRepository.markSubmission(studentName, questionId, mark);
        }
    }
}
