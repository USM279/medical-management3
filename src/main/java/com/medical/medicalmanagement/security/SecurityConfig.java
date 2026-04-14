package com.medical.medicalmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    /**
     * CORS Policy Justification:
     * This is a server-side rendered Thymeleaf application where all pages are
     * generated on the server and served from the same host. We allow both
     * localhost and 127.0.0.1 variants on port 8080 (they resolve to the same
     * machine but browsers treat them as different origins). Only GET and POST
     * methods are permitted since the app exclusively uses standard HTML form
     * submissions — no AJAX, no PUT/DELETE from the frontend.
     * Credentials are allowed to support session cookies used by Spring Security.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:8080",
                "http://127.0.0.1:8080"
        ));
        config.setAllowedMethods(List.of("GET", "POST"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // Only ADMIN can add, edit, delete doctors and patients
                        .requestMatchers("/doctors/add/**", "/doctors/edit/**", "/doctors/delete/**").hasRole("ADMIN")
                        .requestMatchers("/patients/add/**", "/patients/edit/**", "/patients/delete/**").hasRole("ADMIN")

                        // Both ADMIN and USER can view and manage appointments
                        .requestMatchers("/appointments/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/", "/index").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // admin has both ADMIN and USER roles (one person, multiple roles)
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("swe123")
                .roles("ADMIN", "USER")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("swe123")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}