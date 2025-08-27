package com.springboot.RestServices.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class AppConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 🔹 Stateless session
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔹 Authorization
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )

            // 🔹 Add custom JWT filter
            .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)

            // 🔹 Disable CSRF
            .csrf(csrf -> csrf.disable())

            // 🔹 Enable CORS with custom config
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 🔹 Basic & form login (optional)
            .httpBasic(customizer -> {})
            .formLogin(customizer -> {});

        return http.build();
    }

    // 🔹 Centralized CORS config bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:4000",
            "http://localhost:5173",
            "https://shopwithzosh.vercel.app",
            "https://ecommerce-angular-blue.vercel.app"
        ));
        cfg.setAllowedMethods(Collections.singletonList("*"));
        cfg.setAllowCredentials(true);
        cfg.setAllowedHeaders(Collections.singletonList("*"));
        cfg.setExposedHeaders(Arrays.asList("Authorization"));
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
