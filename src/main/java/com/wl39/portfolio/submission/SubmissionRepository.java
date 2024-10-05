package com.wl39.portfolio.submission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    @Query("SELECT DISTINCT s FROM Submission s JOIN FETCH s.question q WHERE s.studentName = :studentName")
    Page<Object[]> findByStudentName(@Param("studentName") String studentName, Pageable pageable);

//    @Query("SELECT DISTINCT s FROM Submission s JOIN FETCH s.question q WHERE s.studentName = :studentName AND (s.marked = -1 OR (q.type = 'm' s.studentAnswer != q.answer)) ")
//    Page<Object[]> findRevisionsByStudentName(@Param("studentName") String studentName, Pageable pageable);
    @Query("SELECT DISTINCT s FROM Submission s JOIN FETCH s.question q WHERE s.studentName = :studentName AND (s.marked = -1 OR (q.type = 'm' AND s.studentAnswer != q.answer))")
    Page<Object[]> findRevisionsByStudentName(@Param("studentName") String studentName, Pageable pageable);


    @Query("SELECT DISTINCT s FROM Submission s JOIN FETCH s.question q WHERE s.studentName = :studentName AND s.marked = 0")
    Page<Object[]> getSAQByStudentName(@Param("studentName")String studentName, Pageable pageable);

    @Query("SELECT DISTINCT s FROM Submission s JOIN FETCH s.question q WHERE s.studentName = :studentName AND s.marked = 1")
    Page<Object[]> findMarkedByStudentName(String studentName, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Submission s SET s.marked = :mark WHERE s.studentName = :studentName AND s.question.id = :questionId")
    void markSubmission(@Param("studentName") String studentName, @Param("questionId") Long questionId, @Param("mark") Integer mark);
}
