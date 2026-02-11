package com.example.simple_inventory.configuration;

import com.example.simple_inventory.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // endpoint register boleh diakses tanpa login
                        .requestMatchers("/api/auth/register").permitAll()

                        // inventory GET : USER atau ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/inventories/**")
                        .hasAnyRole("USER", "ADMIN")

                        // inventory POST : ADMIN saja
                        .requestMatchers(HttpMethod.POST, "/api/inventories/**")
                        .hasAnyRole("USER", "ADMIN")

                        // inventory PUT/PATCH : USER dan ADMIN
                        .requestMatchers(HttpMethod.PUT, "/api/inventories/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/inventories/**")
                        .hasAnyRole("USER", "ADMIN")

                        // inventory DELETE : ADMIN saja
                        .requestMatchers(HttpMethod.DELETE, "/api/inventories/**")
                        .hasRole("ADMIN")

                        // semua endpoint lain : harus login
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userDetailsService);

        return http.build();
    }
}
