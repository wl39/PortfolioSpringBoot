package com.wl39.portfolio.student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentImage {
    private String name;
    private String url;
    private String email;

    public StudentImage(Student student) {
        this.name = student.getName();
        this.url = student.getUser().getImage();
        this.email = student.getUser().getEmail();
    }
}
