package com.wl39.portfolio.simple_submission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}
