package com.wl39.portfolio.user;

import com.wl39.portfolio.student.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email; // 로그인용 이메일
    private String username;
    private String role;
    private String imageURL;
    private Set<String> services = new HashSet<>();

    UserResponse(Long id, String email, String username, String role, String imageURL, Student student) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.imageURL = imageURL;

        student.getSubscriptions().forEach(value -> {
            services.add(value.getService().getName());
        });
    }

    UserResponse(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
        this.username = userEntity.getUsername();
        this.role = userEntity.getRole();
        this.imageURL = userEntity.getImage();

        userEntity.getStudent().getSubscriptions().forEach(value -> services.add(value.getService().getName()));
    }
}
