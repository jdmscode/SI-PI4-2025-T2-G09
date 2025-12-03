package com.puccampinassi.pi4.t2g09.onconnect.controller;

import com.puccampinassi.pi4.t2g09.onconnect.model.Comentario;
import com.puccampinassi.pi4.t2g09.onconnect.model.Post;
import com.puccampinassi.pi4.t2g09.onconnect.service.ComentarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<Comentario> criar(
            @PathVariable Long postId,
            @RequestBody @Valid Comentario comentario) {

        Post post = new Post();
        post.setId(postId);                 // associa o comentário ao post principal
        comentario.setPostPrincipal(post);

        Comentario salvo = comentarioService.salvar(comentario);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public List<Comentario> listarPorPost(@PathVariable Long postId) {
        return comentarioService.listarPorPost(postId);
    }
}
