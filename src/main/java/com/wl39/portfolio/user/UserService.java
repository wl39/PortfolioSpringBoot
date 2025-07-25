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
import java.util.Set;
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

    @Transactional
    public ResponseEntity<String> signup(String email, String username) {
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use!");
        }

        // 1. 유니크한 사용자 이름 생성
        String newUsername = generateUniqueUsername(username);

        // 2. Student 생성 및 저장
        Student student = new Student(newUsername);
        studentRepository.save(student);

        // 3. UserEntity 생성 및 연결
        UserEntity user = new UserEntity();
        user.setUsername(newUsername); // 유니크 이름 사용
        user.setEmail(email);
        user.setPassword(null);
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
        return userRepository.findAll().stream().map((user -> new UserResponse(user.getId(), user.getEmail(), user.getUsername(), user.getRole(), user.getImage(), user.getStudent()))).collect(Collectors.toList());
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


    public Set<Student> getChildren(String name) {
        return userRepository.findByUsername(name).orElseThrow().getChildren();
    }

    public ParentResponse postChildren(String name, List<String> children) {
        UserEntity user = userRepository.findByUsername(name).orElseThrow();

        Set<Student> newChildren = children.stream().map(s -> {
            return studentRepository.findByName(s).orElseThrow();
        }).collect(Collectors.toSet());

        user.getChildren().addAll(newChildren);

        userRepository.save(user);

        return new ParentResponse(user.getEmail(), user.getUsername(), user.getChildren());
    }

    public List<ParentResponse> getAllParents() {
        List<UserEntity> users = userRepository.findByRole("PARENT");


        return users.stream().map(u -> new ParentResponse(u.getEmail(), u.getUsername(), u.getChildren())).collect(Collectors.toList());
    }

    public boolean isMyChild(String username, String name) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Student student = studentRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Student not found"));

        return (user.getRole().equals("PARENT") && user.getChildren().contains(student));
    }

    public UserResponse getFindByName(String name) {
        return new UserResponse(userRepository.findByUsername(name).orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    public Object changeImage(UserImagePatch userImagePatch) {
        UserEntity userEntity = userRepository.findByUsername(userImagePatch.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userEntity.setImage(userImagePatch.getImageUrl());

        return new UserResponse(userRepository.save(userEntity));
    }
}
