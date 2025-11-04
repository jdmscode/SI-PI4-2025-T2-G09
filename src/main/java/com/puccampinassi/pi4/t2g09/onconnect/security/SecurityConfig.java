package com.puccampinassi.pi4.t2g09.onconnect.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // permite POST sem CSRF token
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // libera register/login
                .requestMatchers("/post/**").permitAll() // 👈 libera temporariamente o endpoint (Deve-se remover está linha após os testes)
                .anyRequest().authenticated() // outros endpoints precisam de login
            )
            .httpBasic(); // para autenticação básica em endpoints protegidos

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

