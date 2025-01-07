package com.wl39.portfolio.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByStudentsForContaining(String studentName);
    Page<QuestionStudentDTO> findByStudentsForContaining(Pageable pageable, String studentName);

    @Query("SELECT q FROM Question q JOIN FETCH q.candidates WHERE q.id = :id AND q.name = :name")
    List<QuestionStudentDTO> findQuestionsByStudentName(@Param("name") String name);
}
