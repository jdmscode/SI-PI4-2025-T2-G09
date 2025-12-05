package com.puccampinassi.pi4.t2g09.onconnect.security;

import com.puccampinassi.pi4.t2g09.onconnect.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ProfissionalRepository profissionalRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // libera preflight (CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // login/cadastro
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/profissionais/**").permitAll()

                        // back-end
                        .requestMatchers("/post/**", "/posts/**", "/comentarios/**").permitAll()

                        // front-end (HTML + static)
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/home.html",
                                "/posts.html",
                                "/perfil.html",
                                "/comentarios.html",
                                "/css/**",
                                "/js/**",
                                "/uploads/**"
                        ).permitAll()

                        // qualquer outra coisa precisa estar autenticado
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> profissionalRepository.findByEmail(username)
                .map(prof -> User
                        .withUsername(prof.getEmail())
                        .password(prof.getSenha())
                        .roles("USER")
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Profissional não encontrado: " + username
                ));
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
