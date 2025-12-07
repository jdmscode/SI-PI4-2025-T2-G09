//Jhonatan

package com.puccampinassi.pi4.t2g09.onconnect.controller;

import com.puccampinassi.pi4.t2g09.onconnect.model.Profissional;
import com.puccampinassi.pi4.t2g09.onconnect.service.CrmService;
import com.puccampinassi.pi4.t2g09.onconnect.service.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ProfissionalService profissionalService;
    private final CrmService crmService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Profissional profissional) {

        // 1 — Verificar se já existe email cadastrado
        if (profissionalService.autenticar(profissional.getEmail(), profissional.getSenha()).isPresent()) {
            return ResponseEntity.badRequest().body("Usuário já existe!");
        }

        // 2 — Validar CRM + Nome
        boolean crmValido = crmService.validarCrm(
                profissional.getRegistroProfissional(),
                profissional.getNomeCompleto()
        );

        if (!crmValido) {
            return ResponseEntity
                    .badRequest()
                    .body("CRM inválido ou não encontrado para este nome.");
        }

        // 3 — Prosseguir com cadastro
        Profissional novo = profissionalService.cadastrar(profissional);
        return ResponseEntity.ok(novo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Profissional profissional) {
        return profissionalService.autenticar(profissional.getEmail(), profissional.getSenha())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).body("Credenciais inválidas"));
    }
}
