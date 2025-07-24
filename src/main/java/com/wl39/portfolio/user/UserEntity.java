package com.wl39.portfolio.user;

import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.teacher.Teacher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // 로그인용 이메일

    @Column(nullable = true)
    private String password; // 암호화된 비밀번호

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String role = "STUDENT";

    @Column(nullable = false)
    private String image = "https://img.91b.co.uk/73cfba62-a3fb-474d-a466-befca9268af4.webp";

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @OneToOne
    @JoinColumn(name = "teacher_id", nullable = true)
    private Teacher teacher;

    @ManyToMany
    @JoinTable(name="children", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
            private Set<Student> children = new HashSet<>();
}
