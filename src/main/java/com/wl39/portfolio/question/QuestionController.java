package com.wl39.portfolio.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:3000")
@RequestMapping(path = "api/v1/questions")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public Long postQuestion(@RequestBody QuestionCreateRequest questionCreateRequest) {
        return questionService.postQuestion(questionCreateRequest);
    }

    @PostMapping("/multiples")
    public Long postQuestions(@RequestBody List<QuestionCreateRequest> questionCreateRequest) {
        return questionService.postQuestions(questionCreateRequest);
    }

    @GetMapping("/{name}")
    public Page<Question> getQuestionsPage(Pageable pageable, @PathVariable String name) {
        return questionService.getQuestionsPage(pageable, name);
    }

    @GetMapping("page/{name}")
    public Page<Question> getOptimizedQuestionsPage(Pageable pageable, @PathVariable String name) {
        return questionService.getOptimizedQuestionsPage(pageable, name);
    }

    @GetMapping
    public Page<QuestionOnly> getAllQuestionsPage(Pageable pageable) {
        return questionService.getAllQuestionsPage(pageable);
    }
}
