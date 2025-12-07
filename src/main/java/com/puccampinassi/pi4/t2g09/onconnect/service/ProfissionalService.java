//Jhonatan

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
    private final CrmService crmService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Profissional cadastrar(Profissional profissional) {
        // Validação de CRM + nome comentado para teste
        // boolean crmValido = crmService.validarCrm(
        //         profissional.getRegistroProfissional(),
        //         profissional.getNomeCompleto()
        // );
        // if (!crmValido) {
        //     throw new RuntimeException("CRM inválido, inativo ou nome não confere!");
        // }

        // Criptografar senha
        profissional.setSenha(passwordEncoder.encode(profissional.getSenha()));

        // Salvar no banco
        return repository.save(profissional);
    }

    public Optional<Profissional> autenticar(String email, String senha) {
        Optional<Profissional> profissionalOpt = repository.findByEmail(email);
        if (profissionalOpt.isPresent() && passwordEncoder.matches(senha, profissionalOpt.get().getSenha())) {
            return profissionalOpt;
        }
        return Optional.empty();
    }

    public Long buscarIdPorEmail(String email) {
        return repository.findByEmail(email)
                .map(Profissional::getId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + email));
    }  
}
