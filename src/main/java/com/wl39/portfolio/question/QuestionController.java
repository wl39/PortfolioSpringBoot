package com.wl39.portfolio.question;

import com.wl39.portfolio.user.CustomUserPrincipal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
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

    @GetMapping("student/{name}")
    public Page<Question> getOptimizedQuestionsPage(Pageable pageable, @PathVariable String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !user.getUsername().equals(name)) {
            System.out.println("gas");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
        }

        return questionService.getOptimizedQuestionsPage(pageable, name);
    }

    @GetMapping
    public Page<QuestionOnly> getAllQuestionsPage(Pageable pageable) {
        return questionService.getAllQuestionsPage(pageable);
    }
}
