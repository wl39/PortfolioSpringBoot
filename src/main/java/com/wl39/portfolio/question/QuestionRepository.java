package com.wl39.portfolio.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByStudentsForContaining(String studentName);
    Page<QuestionStudentDTO> findByStudentsForContaining(Pageable pageable, String studentName);
}
