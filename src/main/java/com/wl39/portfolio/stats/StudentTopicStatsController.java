package com.wl39.portfolio.stats;

import com.wl39.portfolio.user.CustomUserPrincipal;
import com.wl39.portfolio.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/v1/stats/topics")
@RequiredArgsConstructor
public class StudentTopicStatsController {
    private final StudentTopicStatsService studentTopicStatsService;
    private final UserService userService;

    @GetMapping(path = "/{name}")
    public ResponseEntity<?> getAll(@PathVariable String name) {
        return ResponseEntity.ok(studentTopicStatsService.getAll(name));
    }

    @GetMapping(path ="/page/{name}")
    public ResponseEntity<?> getAll(Pageable pageable, @PathVariable String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        boolean isParent = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PARENT"));

        if (isAdmin) {
            return ResponseEntity.ok(studentTopicStatsService.getAll(pageable, name));
        }

        if (user.getUsername().equals(name)) {
            return ResponseEntity.ok(studentTopicStatsService.getAll(pageable, name));
        }

        if (isParent) {
            boolean isChild = userService.isMyChild(user.getUsername(), name); // 구현 필요
            if (isChild) {
                return ResponseEntity.ok(studentTopicStatsService.getAll(pageable, name));
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }

    @PatchMapping(path = "/reload/{name}")
    public ResponseEntity<?> reload(@PathVariable String name, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        return ResponseEntity.ok(studentTopicStatsService.reloadStatsForStudent(name));
    }
}
