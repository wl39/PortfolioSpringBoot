package com.wl39.portfolio.stats;

import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.student.StudentRepository;
import com.wl39.portfolio.submission.*;
import com.wl39.portfolio.topic.Topic;
import com.wl39.portfolio.topic.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentTopicStatsService {
    private final StudentTopicStatsRepository studentTopicStatsRepository;
    private final StudentRepository studentRepository;
    private final SubmissionRepository submissionRepository;
    private final TopicRepository topicRepository;

    @Transactional
    public Long reloadStatsForStudent(String name) {
        Student student = studentRepository.findByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found")
        );

        List<SubmissionTopic> submissionTopics = submissionRepository.findByStudentName(name).stream().map(submission -> new SubmissionTopic(
                submission.getQuestion().getId(),
                submission.getMarked(),
                submission.getSubmitDate(),
                submission.getQuestion().getTopics().stream().map(Topic::getTitle).collect(Collectors.toSet())
        )).toList();

        studentTopicStatsRepository.deleteByStudent_Id(student.getId());

        updateStatsForStudent(student, submissionTopics);

        return 1L;
    }

    public void updateStatsForStudent(Student student, List<SubmissionTopic> submissionTopics) {

        for (SubmissionTopic submissionTopic : submissionTopics) {
            boolean isCorrect = submissionTopic.getMarked() == 1;

            for (String title : submissionTopic.getTopics()) {
                Topic topic = topicRepository.findByTitle(title).orElseThrow();

                // Student ID와 Topic ID로 통계 조회
                StudentTopicStats stats = studentTopicStatsRepository.findByStudent_IdAndTopic_Id(student.getId(), topic.getId())
                        .orElseGet(() -> {
                            StudentTopicStats newStats = new StudentTopicStats();
                            newStats.setStudent(student);
                            newStats.setTopic(topic);
                            newStats.setCorrectCount(0L);
                            newStats.setWrongCount(0L);
                            newStats.setTotalCount(0L);
                            return newStats;
                        });

                stats.setSubmitDate(submissionTopic.getSubmitDate());
                stats.setTotalCount(stats.getTotalCount() + 1);

                if (isCorrect) {
                    stats.setCorrectCount(stats.getCorrectCount() + 1);
                } else {
                    stats.setWrongCount(stats.getWrongCount() + 1);
                }

                studentTopicStatsRepository.save(stats);
            }
        }
    }

    public List<StudentTopicStatWithTitle> getAll(String name) {
        return studentTopicStatsRepository.findByStudentName(name);
    }

    public Page<StudentTopicStatWithTitle> getAll(Pageable pageable, String name) {
        return studentTopicStatsRepository.findByStudentName(pageable, name);
    }
}
