package com.wl39.portfolio.question;

import com.wl39.portfolio.assignment.Assignment;
import com.wl39.portfolio.candidate.Candidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        if (question.getType() == 'm')
            for (Candidate c : question.getCandidates()) {
                c.setQuestion(question);
            }

        for (Assignment a : question.getAssignments()) {
            a.setDataFromQuestion(question);
        }

        this.questionRepository.save(question);

        return question.getId();
    }

    public List<Question> findByStudentName(String name) {
        return this.questionRepository.findByAssignments_Id_NameContaining(name);
    }

    public List<QuestionForStudentDTO> findByStudentNameQuestionOnly(String studentName) {
        List<Question> questions = this.questionRepository.findByAssignments_Id_NameContaining(studentName);

        return questions.stream().map(question ->
                    new QuestionForStudentDTO(
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

    public Page<QuestionForStudentDTO> getAllQuestionsOnlyPage(Pageable pageable, String name) {
        Page<Question> questions = questionRepository.findByAssignments_Id_NameContaining(pageable, name);
        return questions.map(question -> new QuestionForStudentDTO(
                question.getId(),
                question.getTitle(),
                question.getQuestion(),
                question.getType(),
                question.getCandidates(),
                question.getHint(),
                question.getGeneratedDate(),
                question.getTargetDate()
        ));
    }

    public Page<Question> getAllQuestionsPage(Pageable pageable) {
        return this.questionRepository.findAll(pageable);
    }

    public List<Question> updateQuestionsWithStudentsFor(List<Long> questionIds, List<String> studentsFor, LocalDateTime targetDate) {
        List<Question> newQuestions = new ArrayList<>();

        // Loop through the question IDs
        for (Long questionId : questionIds) {
            // Find the question by ID
            Question originalQuestion = questionRepository.findById(questionId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid question ID: " + questionId));

            // Create a new Question instance as a replica of the original
            Question newQuestion = new Question(); // 문제 새로 만들 필요 없이 calendar랑 학생들에게 문제만 할당하면 될듯
            newQuestion.setTitle(originalQuestion.getTitle());
            newQuestion.setQuestion(originalQuestion.getQuestion());
            newQuestion.setType(originalQuestion.getType());
            newQuestion.setCandidates(originalQuestion.getCandidates());
            newQuestion.setHint(originalQuestion.getHint());


            // Set the new target date for the new question
            newQuestion.setTargetDate(targetDate);

            // Copy other relevant fields as needed
            newQuestion.setAnswer(originalQuestion.getAnswer());
            newQuestion.setExplanation(originalQuestion.getExplanation());
            newQuestion.setGeneratedDate(LocalDateTime.now()); // or set to original if needed


            // Save the new question
            newQuestions.add(questionRepository.save(newQuestion));
        }

        return newQuestions; // Return the list of newly created questions
    }


}
