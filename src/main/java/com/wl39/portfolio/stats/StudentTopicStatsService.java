package com.wl39.portfolio.stats;

import com.wl39.portfolio.question.QuestionTopic;
import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.submission.*;
import com.wl39.portfolio.topic.Topic;
import com.wl39.portfolio.topic.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentTopicStatsService {
    private final StudentTopicStatsRepository studentTopicStatsRepository;
    private final TopicRepository topicRepository;

    public void reloadStatsForStudent(Student student, List<SubmissionTopic> submissionTopics) {

        for (SubmissionTopic submissionTopic : submissionTopics) {
            boolean isCorrect = submissionTopic.getMarked() == 1;

            for (String title : submissionTopic.getTopicTitles()) {
                Topic topic = topicRepository.findByTitle(title).orElseThrow();

                // Student ID와 Topic ID로 통계 조회
                StudentTopicStats stats = studentTopicStatsRepository.findByStudent_IdAndTopic_Id(student.getId(), topic.getId())
                        .orElseGet(() -> {
                            StudentTopicStats newStats = new StudentTopicStats();
                            newStats.setStudent(student);
                            newStats.setTopic(topic);
                            newStats.setCorrectCount(0L);
                            newStats.setWrongCount(0L);
                            return newStats;
                        });

                stats.setUpdatedAt(LocalDateTime.now());
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
}
