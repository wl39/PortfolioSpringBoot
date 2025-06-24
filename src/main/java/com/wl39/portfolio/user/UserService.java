package com.wl39.portfolio.user;

import com.wl39.portfolio.student.Student;
import com.wl39.portfolio.student.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return User.builder().username(user.getEmail()).password(user.getPassword()).roles(user.getRole()).build();
    }

    public UserEntity findByEmail(String username) {
        return userRepository.findByEmail(username).orElseThrow();
    }

    @Transactional
    public ResponseEntity<String> signup(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use!");
        }

        // 1. 유니크한 사용자 이름 생성
        String newUsername = generateUniqueUsername(signupRequest.getUsername());

        // 2. Student 생성 및 저장
        Student student = new Student(newUsername);
        studentRepository.save(student);

        // 3. UserEntity 생성 및 연결
        UserEntity user = new UserEntity();
        user.setUsername(newUsername); // 유니크 이름 사용
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setStudent(student); //

        userRepository.save(user);

        return ResponseEntity.ok("Signup successful!");
    }

    private String generateUniqueUsername(String baseUsername) {
        String username = baseUsername;

        while (studentRepository.existsByName(username)) {
            String uuidSuffix = UUID.randomUUID().toString().substring(0, 8);
            username = baseUsername + "_" + uuidSuffix;
        }

        return username;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map((user -> new UserResponse(user.getId(), user.getEmail(), user.getUsername(), user.getRole(), user.getStudent()))).collect(Collectors.toList());
    }

    @Transactional
    public UserResponse modifyUser(UserPatch userPatch) {
        // 1. 기존 이메일로 사용자 조회
        UserEntity userEntity = userRepository.findById(userPatch.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // 2. 변경 정보 반영
        userEntity.setEmail(userPatch.getEmail());
        userEntity.setUsername(userPatch.getUsername());
        userEntity.setRole(userPatch.getRole());

        // 3. Student 연결 (이름 기반 말고 연관된 user 기준으로)
        Student student = studentRepository.findById(userEntity.getStudent().getId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        student.setName(userPatch.getUsername());

        studentRepository.save(student);
        return new UserResponse(userRepository.save(userEntity));
    }

}
