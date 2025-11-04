package com.puccampinassi.pi4.t2g09.onconnect.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entidade que representa uma publicação feita por um usuário.
 * Inclui validações automáticas com Jakarta Validation e uso de Lombok
 * para geração de getters, setters e construtores.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título não pode estar vazio.")
    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "O texto não pode estar vazio.")
    @Size(max = 10000, message = "O texto deve ter no máximo 10.000 caracteres.")
    @Column(nullable = false, length = 10000)
    private String texto;

    @Column(nullable = false)
    private int qtdLikes = 0;

    @Column(nullable = false)
    private int qtdDislikes = 0;

}
