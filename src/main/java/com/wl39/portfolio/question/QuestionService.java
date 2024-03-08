package com.wl39.portfolio.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Long uploadQuestion(Question question) {
        this.questionRepository.save(question);

        return question.getId();
    }

    public List<Question> findByStudentName(String studentName) {
        return this.questionRepository.findByStudentsForContaining(studentName);
    }

    public List<QuestionDTO> findByStudentNameQuestionOnly(String studentName) {
        List<Question> questions = this.questionRepository.findByStudentsForContaining(studentName);

        return questions.stream().map(question ->
                    new QuestionDTO(
                            question.getId(),
                            question.getTitle(),
                            question.getQuestion(),
                            question.getType(),
                            question.getCandidates(),
                            question.getHint(),
                            question.getGeneratedDate(),
                            question.getTargetDate()
                    )
                ).collect(Collectors.toList());
    }

    public Page<QuestionDTO> getAllQuestionsOnlyPage(Pageable pageable, String studentName) {
        return this.questionRepository.findByStudentsForContaining(pageable, studentName);
    }

    public Page<Question> getAllQuestionsPage(Pageable pageable) {
        return this.questionRepository.findAll(pageable);
    }
}
