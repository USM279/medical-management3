package com.medical.medicalmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()

                        // DOKTOR ve HASTA yönetimi sadece ADMIN'de kalsın
                        .requestMatchers("/doctors/add/**", "/doctors/edit/**", "/doctors/delete/**").hasRole("ADMIN")
                        .requestMatchers("/patients/add/**", "/patients/edit/**", "/patients/delete/**").hasRole("ADMIN")

                        // RANDEVU: Hem USER hem ADMIN randevu oluşturabilsin ve listeyi görebilsin
                        .requestMatchers("/appointments", "/appointments/add/**").hasAnyRole("ADMIN", "USER")


                        .requestMatchers("/appointments/delete/**").hasAnyRole("ADMIN", "USER")

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
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("swe123")
                .roles("ADMIN")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("swe123")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}