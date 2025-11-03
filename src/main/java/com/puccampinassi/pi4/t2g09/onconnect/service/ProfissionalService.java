package com.puccampinassi.pi4.t2g09.onconnect.service;

import com.puccampinassi.pi4.t2g09.onconnect.model.Profissional;
import com.puccampinassi.pi4.t2g09.onconnect.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Profissional cadastrar(Profissional profissional) {
        // Criptografar senha
        profissional.setSenha(passwordEncoder.encode(profissional.getSenha()));
        return repository.save(profissional);
    }

    public Optional<Profissional> autenticar(String email, String senha) {
        Optional<Profissional> profissionalOpt = repository.findByEmail(email);
        if (profissionalOpt.isPresent() && passwordEncoder.matches(senha, profissionalOpt.get().getSenha())) {
            return profissionalOpt;
        }
        return Optional.empty();
    }
}
