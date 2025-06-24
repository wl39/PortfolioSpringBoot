package com.wl39.portfolio.user;

import com.wl39.portfolio.student.Student;
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

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String role = "STUDENT";

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToMany
    @JoinTable(name="children", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
            private Set<Student> children = new HashSet<>();
}
