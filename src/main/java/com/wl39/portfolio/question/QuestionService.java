package com.wl39.portfolio.question;

import com.wl39.portfolio.assignment.Assignment;
import com.wl39.portfolio.calendar.CalendarService;
import com.wl39.portfolio.candidate.Candidate;
import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.student.StudentRepository;
import com.wl39.portfolio.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final StudentService studentService;
    private final CalendarService calendarService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, StudentService studentService, CalendarService calendarService) {
        this.questionRepository = questionRepository;
        this.studentService = studentService;
        this.calendarService = calendarService;
    }

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
                    question.getGeneratedDate())
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
}
