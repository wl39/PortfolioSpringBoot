package com.wl39.portfolio.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final UserService userService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JWTProvider jwtProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()) // 인증 전 상태
            );

            UserEntity user = userService.findByEmail(authentication.getName());

            String jwt = jwtProvider.generateToken(user.getEmail(), user.getUsername(), user.getRole());

            Cookie cookie = new Cookie("token", jwt);
            cookie.setHttpOnly(true); // 자바스크립트에서 접근 불가
            cookie.setSecure(true); // HTTPS 연결에서만 전송 (개발 중엔 false로 테스트 가능)
            cookie.setPath("/"); // 모든 경로에서 쿠키 사용 가능
            cookie.setMaxAge(3600); // 1시간 (초 단위)

            response.addCookie(cookie);

            return ResponseEntity.ok(user.getUsername());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {

        return userService.signup(signupRequest);
    }

    @GetMapping("/auth-check")
    public ResponseEntity<?> authCheck(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        return ResponseEntity.ok(
                "username: " + user.getUsername() + ", role: " + (isAdmin ? "ADMIN" : "USER"));
    }

}
