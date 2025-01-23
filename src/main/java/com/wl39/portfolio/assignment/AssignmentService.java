package com.wl39.portfolio.assignment;

import com.wl39.portfolio.question.Question;
import com.wl39.portfolio.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, QuestionRepository questionRepository) {
        this.assignmentRepository = assignmentRepository;
        this.questionRepository = questionRepository;
    }


    public List<Assignment> getAssignmentsByNameAndYearMonth(String name, int year, int month) {
        return assignmentRepository.getAssignmentsByNameAndYearMonth(name, year, month);
    }

    @Transactional // Allows ATOMICITY [If error occurs, everything will be rollback]
    public List<Long> assignQuestions(AssignmentDTO assignments) {
        List<Long> savedIds = new ArrayList<>();

        List<String> names = assignments.getNames();
        List<Long> ids = assignments.getIds();
        LocalDateTime targetDate = assignments.getTargetDate();

        // Iterate over provided IDs and Names to create assignments
        for (int i = 0; i < ids.size(); i++) {
            Long questionId = ids.get(i);


            // Fetch the Question entity by ID
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new IllegalArgumentException("Question not found for ID: " + questionId));


            for (int j  = 0; j < names.size(); j++) {
                String name = names.get(j);

                // Create AssignmentID
                AssignmentID assignmentID = new AssignmentID();
                assignmentID.setQuestionId(questionId);
                assignmentID.setName(name);
                assignmentID.setGeneratedDate(LocalDateTime.now());

                // Create Assignment
                Assignment assignment = new Assignment();
                assignment.setId(assignmentID);
                assignment.setQuestion(question);
                assignment.setTargetDate(targetDate);

                // Save Assignment to repository
                assignmentRepository.save(assignment);
                savedIds.add(ids.get(i)); // Add the ID to the response list
            }

        }

        return savedIds; // Return the saved IDs as response
    }
}
