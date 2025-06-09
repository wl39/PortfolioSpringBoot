package com.wl39.portfolio.simple_submission;

import com.wl39.portfolio.submission.SubmissionDayCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SimpleSubmissionRepository extends JpaRepository<SimpleSubmission, Long> {
    long countByName(String name);

    long countByNameAndAnswer(String name, String answer);
    long countByNameAndAnswerNot(String name, String answer);
    @Query("SELECT COUNT(s) FROM SimpleSubmission s WHERE s.name = :name AND s.answer = :answer AND DATE(s.submitDate) = :date")
    long countByNameAndAnswerAndSubmitDate(@Param("name") String name, @Param("answer") String answer, @Param("date") LocalDate submitDate);

    @Query("SELECT COUNT(s) FROM SimpleSubmission s WHERE s.name = :name AND s.answer <> :answer AND DATE(s.submitDate) = :date")
    long countByNameAndAnswerNotAndSubmitDate(@Param("name") String name, @Param("answer") String answer, @Param("date") LocalDate submitDate);


    @Query("SELECT COUNT(s) FROM SimpleSubmission s WHERE s.name = :name AND DATE(s.submitDate) = :date")
    long countByNameAndSubmitDate(@Param("name") String name, @Param("date") LocalDate submitDate);

    //SELECT name, count(answer) as questions, DATE(submit_date) as date  FROM simple_submission ss WHERE answer = "-1" GROUP BY DATE(submit_date);
    @Query("SELECT new com.wl39.portfolio.submission.SubmissionDayCount(" +
            "       s.name, SUM(CASE WHEN s.answer = '-1' THEN 1 ELSE 0 END), SUM(CASE WHEN s.answer <> '-1' THEN 1 ELSE 0 END), FUNCTION('date', s.submitDate)) " +
            "FROM SimpleSubmission s " +
            "WHERE s.name = :name " +
            "GROUP BY FUNCTION('date', s.submitDate)")
    Page<SubmissionDayCount> getDayCountsByName(Pageable pageable, @Param("name") String name);

}
