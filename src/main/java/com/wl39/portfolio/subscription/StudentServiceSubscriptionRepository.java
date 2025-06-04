package com.wl39.portfolio.subscription;

import com.wl39.portfolio.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentServiceSubscriptionRepository extends JpaRepository<StudentServiceSubscription, Long> {
    List<StudentServiceSubscription> findByStudent(Student student);
}
