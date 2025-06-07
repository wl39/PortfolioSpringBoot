package com.wl39.portfolio.subscription;

import com.wl39.portfolio.services.ServiceRepository;
import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.core.RepositoryCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceSubscriptionService {
    private final ServiceRepository serviceRepository;
    private final StudentRepository studentRepository;
    private final StudentServiceSubscriptionRepository subscriptionRepository;

    public ResponseEntity<?> postSubscription(SubscriptionRequest subscriptionRequest) {
        StudentServiceSubscription studentServiceSubscription = new StudentServiceSubscription();

        com.wl39.portfolio.services.Service service = serviceRepository.findByName(subscriptionRequest.getServiceName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));

        Student student = studentRepository.findByName(subscriptionRequest.getStudentName()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        studentServiceSubscription.setService(service);
        studentServiceSubscription.setStudent(student);

        subscriptionRepository.save(studentServiceSubscription);

        return ResponseEntity.ok(1);
    }
    public ResponseEntity<?> postSubscription(List<SubscriptionRequest> subscriptionRequests) {
        List<StudentServiceSubscription> studentServiceSubscriptions = new ArrayList<>();

        subscriptionRequests.forEach(this::postSubscription);

        return ResponseEntity.ok(subscriptionRequests.size());
    }

    public ResponseEntity<?> getSubscriptions(String name) {
        Student student = studentRepository.findByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found")
        );

        List<String> subscriptions = new ArrayList<>();

        for (StudentServiceSubscription subscription : subscriptionRepository.findByStudent(student)) {
            subscriptions.add(subscription.getService().getName());
        }

        return ResponseEntity.ok(subscriptions);
    }

}
