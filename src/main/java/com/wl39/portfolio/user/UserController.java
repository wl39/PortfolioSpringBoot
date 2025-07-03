package com.wl39.portfolio.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    @Value("${IS_PROD}")
    private boolean isProd;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final UserService userService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JWTProvider jwtProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Delete accessToken cookie
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setMaxAge(0);

        // Delete refreshToken cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok("Logged out");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserEntity user = userService.findByEmail(authentication.getName());

            // access + refresh
            String accessToken = jwtProvider.generateToken(user.getEmail(), user.getUsername(), user.getRole());
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

            // access token
            Cookie accessCookie = new Cookie("token", accessToken);
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(!isProd);
            accessCookie.setPath("/");
            accessCookie.setMaxAge(60 * 60); // 1 hour

            // refresh token
            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(!isProd);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

            // Add cookies
            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);

            return ResponseEntity.ok(user.getUsername());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String email = jwtProvider.getEmailFromJWT(refreshToken);

        UserEntity user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // 새 access token 생성
        String newAccessToken = jwtProvider.generateToken(
                user.getEmail(), user.getUsername(), user.getRole()
        );

        Cookie accessCookie = new Cookie("token", newAccessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(!isProd);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(60 * 60); // 1시간

        response.addCookie(accessCookie);

        return ResponseEntity.ok(user.getUsername());
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

    @GetMapping(path = "children/{name}")
    public ResponseEntity<?> getChildren(@PathVariable String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        boolean isAllowed = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_PARENT"));

        if (isAllowed) {
            return ResponseEntity.ok(userService.getChildren(name));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }

    @PostMapping(path = "children/{name}")
    public ResponseEntity<?> postChildren(@PathVariable String name, @RequestBody List<String> children, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        boolean isAllowed = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_PARENT"));

        if (isAllowed) {
            return ResponseEntity.ok(userService.postChildren(name, children));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }

    @GetMapping(path = "parents")
    public ResponseEntity<?> getAllParents() {
        return ResponseEntity.ok(userService.getAllParents());
    }

    @PatchMapping
    public ResponseEntity<?> modify(@RequestBody UserPatch userPatch) {
        return ResponseEntity.ok(userService.modifyUser(userPatch));
    }

    @PatchMapping(path = "/image")
    public ResponseEntity<?> changeImage(@RequestBody UserImagePatch imageUrl) {
        return ResponseEntity.ok(userService.changeImage(imageUrl));
    }
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.getFindByName(name));
    }
}
