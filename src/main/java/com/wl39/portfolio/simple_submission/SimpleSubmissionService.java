package com.wl39.portfolio.simple_submission;

import com.wl39.portfolio.submission.SubmissionDayCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class SimpleSubmissionService {
    private final SimpleSubmissionRepository simpleSubmissionRepository;

    @Autowired
    public SimpleSubmissionService(SimpleSubmissionRepository simpleSubmissionRepository) {
        this.simpleSubmissionRepository = simpleSubmissionRepository;
    }

    public Long uploadSubmission(SimpleSubmission ss) {
        this.simpleSubmissionRepository.save(ss);


        return ss.getId();
    }

    public Page<SimpleSubmission> getSubmissions(Pageable pageable) {
       return this.simpleSubmissionRepository.findAll(pageable);
    }

    public Long getCounts(String name) {
        return this.simpleSubmissionRepository.countByName(name);
    }

    public Long getCounts(String name, LocalDate submitDate) {
        return this.simpleSubmissionRepository.countByNameAndSubmitDate(name, submitDate);
    }

    public Long getWrongCounts(String name) {
        return this.simpleSubmissionRepository.countByNameAndAnswer(name, "-1");
    }

    public Long getRightCounts(String name) {
        return this.simpleSubmissionRepository.countByNameAndAnswerNot(name, "-1");
    }

    public Long getWrongCounts(String name, LocalDate submitDate) {
        return this.simpleSubmissionRepository.countByNameAndAnswerAndSubmitDate(name, "-1", submitDate);
    }

    public Long getRightCounts(String name, LocalDate submitDate) {
        return this.simpleSubmissionRepository.countByNameAndAnswerNotAndSubmitDate(name, "-1", submitDate);
    }

    public Page<SubmissionDayCount> getDayCountsByName(Pageable pageable, String name) {
        return this.simpleSubmissionRepository.getDayCountsByName(pageable, name);
    }
}
