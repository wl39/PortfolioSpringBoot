package com.wl39.portfolio.user;

import com.wl39.portfolio.student.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParentResponse {
    private String email;
    private String username;
    private Set<Student> children;
}