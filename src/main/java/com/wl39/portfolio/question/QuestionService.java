package com.wl39.portfolio.question;

import com.wl39.portfolio.assignment.Assignment;
import com.wl39.portfolio.calendar.CalendarService;
import com.wl39.portfolio.candidate.Candidate;
import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.student.StudentService;
import com.wl39.portfolio.topic.Topic;
import com.wl39.portfolio.topic.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final StudentService studentService;
    private final CalendarService calendarService;
    private final TopicRepository topicRepository;


    @Transactional
    public Long postQuestion(QuestionCreateRequest questionCreateRequest) {
        Question question = new Question();

        question.setTitle(questionCreateRequest.getTitle());
        question.setQuestion(questionCreateRequest.getQuestion());
        question.setType(questionCreateRequest.getType());
        question.setHint(questionCreateRequest.getHint());
        question.setAnswer(questionCreateRequest.getAnswer());
        question.setExplanation(questionCreateRequest.getExplanation());
        question.setGeneratedDate(questionCreateRequest.getGeneratedDate());

        List<Assignment> assignments = new ArrayList<>();
        List<Candidate> candidates = new ArrayList<>();

        for (String value : questionCreateRequest.getCandidates()) {
            Candidate candidate = new Candidate();

            candidate.setValue(value);
            candidate.setQuestion(question);

            candidates.add(candidate);
        }

        question.setCandidates(candidates);

        for (String name : questionCreateRequest.getStudents()) {
            Assignment assignment = new Assignment();

            Student student = studentService.get(name);

            assignment.setStudent(student);
            assignment.setQuestion(question);
            assignment.setTargetDate(questionCreateRequest.getTargetDate());

            assignments.add(assignment);

            calendarService.assignNewQuestion(student, questionCreateRequest.getTargetDate().toLocalDate());
        }


        question.setAssignments(assignments);


        return questionRepository.save(question).getId();
    }

    public Page<Question> getQuestionsPage(Pageable pageable, String name) {

        return questionRepository.findByAssignments_Student_Name(pageable, name);
    }

    public Page<Question> getOptimizedQuestionsPage(Pageable pageable, String name) {

        return questionRepository.findByStudentNameFetchAssignments(pageable, name);
    }

    public Page<QuestionOnly> getAllQuestionsPage(Pageable pageable) {
        Page<Question> questions = questionRepository.findAll(pageable);

        return questions.map((question) ->
                new QuestionOnly(
                        question.getId(),
                        question.getTitle(),
                        question.getQuestion(),
                        question.getType(),
                        question.getCandidates(),
                        question.getHint(),
                        question.getAnswer(),
                        question.getExplanation(),
                        question.getGeneratedDate(),
                        question.getTopics())
        );
    }

    public Question get(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow();
    }

    @Transactional
    public Long postQuestions(List<QuestionCreateRequest> questionCreateRequests) {
        List<Question> questions = new ArrayList<>();

        for (QuestionCreateRequest questionCreateRequest : questionCreateRequests) {
            Question question = new Question();

            question.setTitle(questionCreateRequest.getTitle());
            question.setQuestion(questionCreateRequest.getQuestion());
            question.setType(questionCreateRequest.getType());
            question.setHint(questionCreateRequest.getHint());
            question.setAnswer(questionCreateRequest.getAnswer());
            question.setExplanation(questionCreateRequest.getExplanation());
            question.setGeneratedDate(questionCreateRequest.getGeneratedDate());

            List<Assignment> assignments = new ArrayList<>();
            List<Candidate> candidates = new ArrayList<>();

            for (String value : questionCreateRequest.getCandidates()) {
                Candidate candidate = new Candidate();

                candidate.setValue(value);
                candidate.setQuestion(question);

                candidates.add(candidate);
            }

            question.setCandidates(candidates);

            for (String name : questionCreateRequest.getStudents()) {
                Assignment assignment = new Assignment();

                Student student = studentService.get(name);

                assignment.setStudent(student);
                assignment.setQuestion(question);
                assignment.setTargetDate(questionCreateRequest.getTargetDate());

                assignments.add(assignment);

                calendarService.assignNewQuestion(student, questionCreateRequest.getTargetDate().toLocalDate());
            }


            question.setAssignments(assignments);

            questions.add(question);
        }


        return (long) questionRepository.saveAll(questions).size();
    }

    public ResponseEntity<?> patchQuestions(List<QuestionUpdateRequest> questionUpdateRequests) {

        List<Question> questions = new ArrayList<>();

        try {
            for (QuestionUpdateRequest request : questionUpdateRequests) {
                Question question = questionRepository.findById(request.getId()).orElseThrow(NoSuchElementException::new);
                List<Candidate> candidates = new ArrayList<>();

                if (request.getTitle() != null && !request.getTitle().isBlank()) {
                    question.setTitle(request.getTitle());
                }
                if (request.getQuestion() != null && !request.getQuestion().isBlank()) {
                    question.setQuestion(request.getQuestion());
                }
                if (request.getHint() != null && !request.getHint().isBlank()) {
                    question.setHint(request.getHint());
                }
                if (request.getAnswer() != null && !request.getAnswer().isBlank()) {
                    question.setAnswer(request.getAnswer());
                }
                if (request.getExplanation() != null && !request.getExplanation().isBlank()) {
                    question.setExplanation(request.getExplanation());
                }

                if (request.getType() != null) {
                    question.setType(request.getType());
                }

                if (request.getGeneratedDate() != null) {
                    question.setGeneratedDate(request.getGeneratedDate());
                }

                if (request.getCandidates() != null) {
                    List<Candidate> existingCandidates = question.getCandidates();
                    List<String> newValues = request.getCandidates();

                    int i = 0;
                    for (; i < Math.min(existingCandidates.size(), newValues.size()); i++) {
                        Candidate candidate = existingCandidates.get(i);
                        candidate.setValue(newValues.get(i)); // 값만 교체
                    }

                    for (; i < newValues.size(); i++) {
                        Candidate newCandidate = new Candidate();
                        newCandidate.setValue(newValues.get(i));
                        newCandidate.setQuestion(question); // 관계 설정
                        existingCandidates.add(newCandidate);
                    }

                    while (existingCandidates.size() > newValues.size()) {
                        existingCandidates.remove(existingCandidates.size() - 1);
                    }
                }


                questions.add(question);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


        return ResponseEntity.ok((long) questionRepository.saveAll(questions).size());
    }

    public ResponseEntity<?> patchTopics(UpdateQuestionsTopicsRequest updateQuestionsTopicsRequest) {
        List<Question> questions = new ArrayList<>();

        for (Long questionId : updateQuestionsTopicsRequest.getQuestionIds()) {
            Question question = questionRepository.findById(questionId).orElseThrow(() -> new EntityNotFoundException("Cannot find the question"));

            for (String title : updateQuestionsTopicsRequest.getTopics()) {
                Topic topic = topicRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Cannot find the topic"));

                topic.getQuestions().add(question);
                question.getTopics().add(topic);
            }

            questions.add(question);
        }


        return ResponseEntity.ok(questionRepository.saveAll(questions));
    }

    public Page<QuestionTopic> getAllQuestionTopics(Pageable pageable) {
        return questionRepository.findAll(pageable).map((question -> new QuestionTopic(
                question.getId(),
                question.getTitle(),
                question.getQuestion(),
                question.getTopics().stream().map(Topic::getTitle).collect(Collectors.toSet())
        )));
    }

    public Page<QuestionTopic> getTopicsWithTitle(Pageable pageable, String title) {
        return questionRepository.findByTitle(pageable, title).map((question -> new QuestionTopic(
                question.getId(),
                question.getTitle(),
                question.getQuestion(),
                question.getTopics().stream().map(Topic::getTitle).collect(Collectors.toSet())
        )));
    }
}
