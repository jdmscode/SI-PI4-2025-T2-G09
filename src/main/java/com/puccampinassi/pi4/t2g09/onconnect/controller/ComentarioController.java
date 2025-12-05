package com.puccampinassi.pi4.t2g09.onconnect.controller;

import com.puccampinassi.pi4.t2g09.onconnect.dto.ComentarioDto;
import com.puccampinassi.pi4.t2g09.onconnect.service.ComentarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    // GET /posts/{postId}/comentarios
    @GetMapping("/{postId}/comentarios")
    public List<ComentarioDto> listarPorPost(@PathVariable Long postId) {
        return comentarioService.listarPorPost(postId);
    }

    // POST /posts/{postId}/comentarios
    @PostMapping("/{postId}/comentarios")
    public ComentarioDto criarComentario(
            @PathVariable Long postId,
            @RequestBody NovoComentarioRequest request
    ) {
        return comentarioService.criarComentario(
                postId,
                request.autor().id(),
                request.texto()
        );
    }

    public record NovoComentarioRequest(String texto, Autor autor) {
        public record Autor(Long id) {}
    }
}

