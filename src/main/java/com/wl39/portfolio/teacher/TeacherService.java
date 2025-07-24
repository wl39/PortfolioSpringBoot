package com.wl39.portfolio.teacher;

import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.student.StudentImage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public List<StudentImage> getAllStudents(String name) {
        return teacherRepository.findByUsername(name).orElseThrow(() -> new EntityNotFoundException("Teacher not found")).getStudents().stream().map(StudentImage::new).collect(Collectors.toList());
    }
}
