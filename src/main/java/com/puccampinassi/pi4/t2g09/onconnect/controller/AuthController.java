package com.puccampinassi.pi4.t2g09.onconnect.controller;

import com.puccampinassi.pi4.t2g09.onconnect.model.Profissional;
import com.puccampinassi.pi4.t2g09.onconnect.service.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ProfissionalService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Profissional profissional) {
        if (service.autenticar(profissional.getEmail(), profissional.getSenha()).isPresent()) {
            return ResponseEntity.badRequest().body("Usuário já existe!");
        }
        Profissional novo = service.cadastrar(profissional);
        return ResponseEntity.ok(novo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Profissional profissional) {
    return service.autenticar(profissional.getEmail(), profissional.getSenha())
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(401).body("Credenciais inválidas"));
}

}
