package com.wl39.portfolio.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        // accessToken 삭제 쿠키
        ResponseCookie.ResponseCookieBuilder accessBuilder = ResponseCookie.from("token", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0); // 즉시 만료

        // refreshToken 삭제 쿠키
        ResponseCookie.ResponseCookieBuilder refreshBuilder = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0); // 즉시 만료

        if (isProd) {
            accessBuilder
                    .secure(true)
                    .sameSite("None")
                    .domain(".91b.co.uk");

            refreshBuilder
                    .secure(true)
                    .sameSite("None")
                    .domain(".91b.co.uk");
        } else {
            accessBuilder
                    .secure(false)
                    .sameSite("Lax");

            refreshBuilder
                    .secure(false)
                    .sameSite("Lax");
        }

        ResponseCookie accessCookie = accessBuilder.build();
        ResponseCookie refreshCookie = refreshBuilder.build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.ok("Logged out");
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        System.out.println("hi");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserEntity user = userService.findByEmail(authentication.getName());

            // access + refresh
            String accessToken = jwtProvider.generateToken(user.getEmail(), user.getUsername(), user.getRole());
            String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

            // 공통 ResponseCookie 설정 빌더
            ResponseCookie.ResponseCookieBuilder accessBuilder = ResponseCookie.from("token", accessToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofHours(1));

            ResponseCookie.ResponseCookieBuilder refreshBuilder = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(7));

            if (isProd) {
                accessBuilder
                        .secure(true)
                        .sameSite("None")
                        .domain(".91b.co.uk");

                refreshBuilder
                        .secure(true)
                        .sameSite("None")
                        .domain(".91b.co.uk");
            } else {
                accessBuilder
                        .secure(false)
                        .sameSite("Lax");

                refreshBuilder
                        .secure(false)
                        .sameSite("Lax");
            }

            // 최종 쿠키 빌드 및 설정
            ResponseCookie accessCookie = accessBuilder.build();
            ResponseCookie refreshCookie = refreshBuilder.build();

            response.addHeader("Set-Cookie", accessCookie.toString());
            response.addHeader("Set-Cookie", refreshCookie.toString());

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

        ResponseCookie tokenCookie;

        if (isProd) {
            tokenCookie = ResponseCookie.from("token", newAccessToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .domain(".91b.co.uk")
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .build();
        } else {
            tokenCookie = ResponseCookie.from("token", newAccessToken)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax") // 개발 환경에서는 기본값
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .build();
        }

        response.addHeader("Set-Cookie", tokenCookie.toString());

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

        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new AuthCheckResponse(user.getUsername(), roles));
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
    public ResponseEntity<?> getUserByName(@PathVariable String name, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        boolean isParent = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PARENT"));

        if (isAdmin) {
            return ResponseEntity.ok(userService.getFindByName(name));
        }

        if (user.getUsername().equalsIgnoreCase(name)) {
            return ResponseEntity.ok(userService.getFindByName(name));
        }

        if (isParent) {
            boolean isChild = userService.isMyChild(user.getUsername(), name); // 구현 필요
            if (isChild) {
                return ResponseEntity.ok(userService.getFindByName(name));
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }

    @PostMapping("/oauth/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String token = request.get("token");

        try {
            DecodedJWT jwt = jwtProvider.verifyToken(token);
//
            String email = jwt.getClaim("email").asString();
            String username = jwt.getClaim("username").asString();

//            String email = request.get("email");
//            String username = request.get("username");


            userService.signup(email, username);

            String accessToken = jwtProvider.generateToken(email, username, "STUDENT");
            String refreshToken = jwtProvider.generateRefreshToken(email);

            // 공통 ResponseCookie 설정 빌더
            ResponseCookie.ResponseCookieBuilder accessBuilder = ResponseCookie.from("token", accessToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofHours(1));

            ResponseCookie.ResponseCookieBuilder refreshBuilder = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(7));

            if (isProd) {
                accessBuilder
                        .secure(true)
                        .sameSite("None")
                        .domain(".91b.co.uk");

                refreshBuilder
                        .secure(true)
                        .sameSite("None")
                        .domain(".91b.co.uk");
            } else {
                accessBuilder
                        .secure(false)
                        .sameSite("Lax");

                refreshBuilder
                        .secure(false)
                        .sameSite("Lax");
            }

            // 최종 쿠키 빌드 및 설정
            ResponseCookie accessCookie = accessBuilder.build();
            ResponseCookie refreshCookie = refreshBuilder.build();

            response.addHeader("Set-Cookie", accessCookie.toString());
            response.addHeader("Set-Cookie", refreshCookie.toString());

            return ResponseEntity.ok(username);

        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalidate Token");
        }
    }
}
