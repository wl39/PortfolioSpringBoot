package com.wl39.portfolio.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="https://91b.co.uk")
@RequestMapping(path = "api/v1/questions")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public Page<Question> getAllQuestionsPage(Pageable pageable) {
        return this.questionService.getAllQuestionsPage(pageable);
    }

    @GetMapping("/{name}")
    public List<Question> getAllQuestionsByStudentName(@PathVariable String name) {
        return this.questionService.findByStudentName(name);
    }

    @GetMapping("/q/{studentName}")
    public List<QuestionForStudentDTO> getAllQuestionsOnlyPage(@PathVariable String studentName) {
        return this.questionService.findByStudentNameQuestionOnly(studentName);
    }

    @GetMapping("/page/{studentName}")
    public Page<QuestionForStudentDTO> getAllQuestionsOnlyPage(Pageable pageable, @PathVariable String studentName) {
        return this.questionService.getAllQuestionsOnlyPage(pageable, studentName);
    }

    @PostMapping
    public Long uploadQuestion(@RequestBody Question question) {
        this.questionService.uploadQuestion(question);

        return question.getId();
    }

    @PostMapping("/multiples")
    public List<Question> updateMultipleQuestionsWithStudentsFor(@RequestBody QuestionsPostStudentsDTO questionsPostStudentsDTO) {
        return questionService.updateQuestionsWithStudentsFor(questionsPostStudentsDTO.getQuestionIds(), questionsPostStudentsDTO.getStudentsFor(), questionsPostStudentsDTO.getTargetDate());
    }
}
