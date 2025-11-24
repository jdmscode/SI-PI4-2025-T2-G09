package com.puccampinassi.pi4.t2g09.onconnect.model.reacao;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_reacao", length = 32)
@Table(
    name = "reacoes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_usuario_origem_tipo",
            columnNames = {"id_usuario", "id_origem", "tipo_reacao"}
        )
    },
    indexes = {
        @Index(name = "idx_reacoes_origem", columnList = "id_origem"),
        @Index(name = "idx_reacoes_usuario", columnList = "id_usuario")
    }
)
public abstract class Reacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reacao")
    private Long idReacao;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_origem", nullable = false)
    private Long idOrigem;
}
