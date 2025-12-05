package com.puccampinassi.pi4.t2g09.onconnect.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma publicação feita por um usuário.
 * Inclui validações automáticas com Jakarta Validation e uso de Lombok
 * para geração de getters, setters e construtores.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional autor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
