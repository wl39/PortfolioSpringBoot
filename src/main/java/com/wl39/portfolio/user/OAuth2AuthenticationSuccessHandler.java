package com.wl39.portfolio.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${IS_PROD}")
    private boolean isProd;
    @Value("${CORS_ALLOWED_ORIGINS}")
    private String url;
    private final UserRepository userRepository;  // 신규 사용자 저장
    private final JWTProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse res,
                                        Authentication auth) throws IOException {
        DefaultOAuth2User oauthUser = (DefaultOAuth2User) auth.getPrincipal();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) auth;

        String registrationId = oauthToken.getAuthorizedClientRegistrationId();

        System.out.println(registrationId);

        String email = "";
        String username = "";

        switch (registrationId) {
            case "naver" -> {
                Map<String, Object> response = oauthUser.getAttribute("response");

                email = response.get("email").toString();
                username = response.get("name").toString();
            }
            case "kakao" -> {
                Map<String, Object> response = (Map<String, Object>) oauthUser.getAttribute("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) response.get("profile");

                email = response.get("email").toString();
                username = profile.get("nickname").toString();
            }
            case "google" -> {
                email = oauthUser.getAttribute("email");
                username = oauthUser.getAttribute("name");
            }
        }

        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            String token = jwtProvider.issueRegisterToken(email, registrationId, username);
            String redirectUrl = UriComponentsBuilder
                    .fromUriString(url + "/oauth2/register")
                    .queryParam("token", token).build().toUriString();

            res.sendRedirect(redirectUrl);
        } else {
            String accessToken = jwtProvider.generateToken(user.get().getEmail(), user.get().getUsername(), user.get().getRole());
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

            res.addHeader("Set-Cookie", accessCookie.toString());
            res.addHeader("Set-Cookie", refreshCookie.toString());

            res.sendRedirect(url + "/user/" + URLEncoder.encode(user.get().getUsername(), StandardCharsets.UTF_8));
        }

//        System.out.println(Objects.requireNonNull(oauthUser.getAttribute("response")).toString());
//        User user = userRepository.findByEmail(email)
//                .orElseGet(() -> userRepository.save(
//                        User.fromOAuth(oauthUser)));
//        String token = jwtService.issueToken(user);
        // Front‑end로 토큰을 쿼리스트링 또는 응답 헤더로 전달

//        res.sendRedirect("http://localhost:3000/oauth2/redirect?token=" + token);
    }
}
