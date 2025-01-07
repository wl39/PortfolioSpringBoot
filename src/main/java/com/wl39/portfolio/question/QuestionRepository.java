package com.wl39.portfolio.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByAssignments_Id_NameContaining(String name);
//    List<Question> findByStudentsForContaining(String studentName);
    Page<QuestionStudentDTO> findByAssignments_Id_NameContaining(Pageable pageable, String name);

    @Query("SELECT q FROM Question q JOIN FETCH q.assignments a WHERE a.question = q AND a.id.name = :name")
    List<QuestionStudentDTO> findQuestionsByStudentName(@Param("name") String name);
}
