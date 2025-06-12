package com.wl39.portfolio.question;

import com.wl39.portfolio.user.CustomUserPrincipal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/questions")
@Validated
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public Long postQuestion(@RequestBody @Valid QuestionCreateRequest questionCreateRequest) {
        return questionService.postQuestion(questionCreateRequest);
    }

    @PostMapping("/multiples")
    public Long postQuestions(@RequestBody List<@Valid QuestionCreateRequest> questionCreateRequest) {
        return questionService.postQuestions(questionCreateRequest);
    }

    @PatchMapping("/multiples")
    public ResponseEntity<?> patchQuestions(@RequestBody List<@Valid QuestionUpdateRequest> questionUpdateRequests) {
        return questionService.patchQuestions(questionUpdateRequests);
    }

    @GetMapping("/{name}")
    public Page<Question> getQuestionsPage(Pageable pageable, @PathVariable String name) {
        return questionService.getQuestionsPage(pageable, name);
    }

    @GetMapping("student/{name}")
    public ResponseEntity<?> getOptimizedQuestionsPage(Pageable pageable, @PathVariable String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !user.getUsername().equals(name)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        Page<Question> page = questionService.getOptimizedQuestionsPage(pageable, name);


        return page.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(page);
    }

    @GetMapping
    public Page<QuestionOnly> getAllQuestionsPage(Pageable pageable) {
        return questionService.getAllQuestionsPage(pageable);
    }

    @GetMapping("topics")
    public ResponseEntity<?> getTopics(Pageable pageable) {
        return  ResponseEntity.ok(questionService.getAllQuestionTopics(pageable));
    }

    @PatchMapping("topics")
    public ResponseEntity<?> patchTopics(@RequestBody @Valid UpdateQuestionTopicsRequest updateQuestionTopicsRequest) {
        return questionService.patchTopics(updateQuestionTopicsRequest);
    }

    @GetMapping("topics/search")
    public ResponseEntity<?> getTopicsWithTitle(Pageable pageable, @RequestParam String title) {
        return ResponseEntity.ok(questionService.getTopicsWithTitle(pageable, title));
    }
}
