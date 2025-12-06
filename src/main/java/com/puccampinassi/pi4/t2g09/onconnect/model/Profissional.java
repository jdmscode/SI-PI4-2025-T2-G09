package com.puccampinassi.pi4.t2g09.onconnect.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profissionais")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nomeCompleto;

    @NotBlank
    @Column(unique = true)
    private String cpf;

    @NotBlank
    private String especialidade;

    @NotBlank
    private String registroProfissional;

    private String instituicao;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String senha;
}
