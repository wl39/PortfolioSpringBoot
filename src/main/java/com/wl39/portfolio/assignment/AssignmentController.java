package com.wl39.portfolio.assignment;

import com.wl39.portfolio.user.CustomUserPrincipal;
import com.wl39.portfolio.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping(path = "api/v1/assignments")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final UserService userService;

    @PostMapping
    public void assignQuestions(@RequestBody @Valid AssignmentsDTO assignmentsDTO) {
        assignmentService.assignQuestions(assignmentsDTO);
    }

    @GetMapping(path = "/latest/{name}")
    public ResponseEntity<?> getLatestDate(@PathVariable String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        boolean isParent = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PARENT"));

        if (isAdmin) {
            return ResponseEntity.ok(assignmentService.getLatestDate(name));
        }

        if (user.getUsername().equalsIgnoreCase(name)) {
            return ResponseEntity.ok(assignmentService.getLatestDate(name));
        }

        if (isParent) {
            boolean isChild = userService.isMyChild(user.getUsername(), name); // 구현 필요
            if (isChild) {
                return ResponseEntity.ok(assignmentService.getLatestDate(name));
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }
}
