// Vitor 

package com.puccampinassi.pi4.t2g09.onconnect.dto;

import java.time.LocalDateTime;

public record ComentarioDto(
        Long id,
        String texto,
        LocalDateTime createdAt,
        Long autorId,
        ProfissionalDto autor
) {}
