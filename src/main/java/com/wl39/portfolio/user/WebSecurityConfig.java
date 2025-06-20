package com.wl39.portfolio.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${CORS_ALLOWED_ORIGINS}")
    private String allowedOrigins;
    private final JWTAuthEntryPoint unauthorizedHandler;
    private final JWTProvider jwtProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigins));
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(jwtProvider);

        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler).accessDeniedHandler(customAccessDeniedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/users/login", "/api/v1/users/signup", "/api/v1/users/refresh").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/questions/student/**").hasAnyRole("ADMIN", "STUDENT", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/assignments/**").hasAnyRole("ADMIN", "STUDENT", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/calendars/**").hasAnyRole("ADMIN", "STUDENT", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/submissions/**").hasAnyRole("ADMIN", "STUDENT", "TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/submissions/**").hasAnyRole("ADMIN", "STUDENT", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/simple_math/**").hasAnyRole("ADMIN", "STUDENT", "TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/simple_math/**").hasAnyRole("ADMIN", "STUDENT", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/subscriptions/**").hasAnyRole("ADMIN", "STUDENT", "TEACHER")


                        .requestMatchers("/api/v1/teacher", "/api/v1/calendar/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/questions/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/questions/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/subscriptions/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/services/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/questions/**").hasAnyRole("ADMIN", "TEACHER")

                        .requestMatchers(HttpMethod.GET, "/api/v1/**").hasAnyRole("ADMIN", "TEACHER")

                        .requestMatchers("/api/v1/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
