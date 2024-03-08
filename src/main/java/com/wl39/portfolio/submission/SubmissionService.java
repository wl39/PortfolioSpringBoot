package com.wl39.portfolio.submission;

import com.wl39.portfolio.question.Question;
import com.wl39.portfolio.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final QuestionRepository questionRepository;
    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository, QuestionRepository questionRepository) {
        this.submissionRepository = submissionRepository;
        this.questionRepository = questionRepository;
    }

    public void addSubmissions(List<SubmissionDTO> submissons) {
        for (SubmissionDTO submission : submissons) {
            Long questionId = submission.getQuestionId();
            String studentAnswer = submission.getStudentAnswer();
            String studentName = submission.getStudentName();
            LocalDateTime submitDate = submission.getSubmitDate();

            Question question = this.questionRepository.findById(questionId).orElse(null);

            if (question != null) {
                List<String> studentsFor = question.getStudentsFor();
                if (studentAnswer != null && studentsFor.contains(studentName)) {
                    studentsFor.remove(studentName);
                    question.setStudentsFor(studentsFor);
                    this.questionRepository.save(question);
                }
                Submission newSubmission = new Submission();
                newSubmission.setQuestion(question);
                newSubmission.setStudentAnswer(studentAnswer);
                newSubmission.setStudentName(studentName);
                newSubmission.setSubmitDate(submitDate);

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

    public Page<Object[]> findByStudentName(String studentName, Pageable pageable) {
//        Page<Object[]> submissionPage = submissionRepository.findByStudentName(studentName, pageable);
//
//        return submissionPage.map()
        return this.submissionRepository.findByStudentName(studentName, pageable);
    }
}
