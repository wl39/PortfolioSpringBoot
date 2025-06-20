package com.wl39.portfolio.stats;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentTopicStatsRepository extends JpaRepository<StudentTopicStats, Long> {
    Optional<StudentTopicStats> findByStudent_IdAndTopic_Id(Long studentId, Long topicId);


    @Query("""
            SELECT new com.wl39.portfolio.stats.StudentTopicStatWithTitle(
                s.topic.title, s.correctCount, s.wrongCount, s.totalCount
            )
            FROM StudentTopicStats s
            JOIN  s.student stu
            WHERE stu.name = :name
            """)
    List<StudentTopicStatWithTitle> findByStudentName(@Param("name") String name);

    @Query("""
            SELECT new com.wl39.portfolio.stats.StudentTopicStatWithTitle(
                s.topic.title, s.correctCount, s.wrongCount, s.totalCount
            )
            FROM StudentTopicStats s
            JOIN  s.student stu
            WHERE stu.name = :name
            """)
    Page<StudentTopicStatWithTitle> findByStudentName(Pageable pageable, @Param("name") String name);

    void deleteByStudent_Id(Long studentId);
}
