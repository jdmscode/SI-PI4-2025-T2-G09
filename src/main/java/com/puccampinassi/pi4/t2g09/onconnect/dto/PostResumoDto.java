package com.puccampinassi.pi4.t2g09.onconnect.dto;

import java.time.LocalDateTime;

public record PostResumoDto(
        Long id,
        String titulo,
        String texto,
        int qtdLikes,
        int qtdDislikes,
        Long autorId,
        String nomeCompleto,
        LocalDateTime createdAt
) {}
