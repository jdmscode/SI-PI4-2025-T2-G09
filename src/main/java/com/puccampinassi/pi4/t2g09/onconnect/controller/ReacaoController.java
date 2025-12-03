package com.puccampinassi.pi4.t2g09.onconnect.controller;

import com.puccampinassi.pi4.t2g09.onconnect.service.ReacaoService;
import com.puccampinassi.pi4.t2g09.onconnect.service.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/reacao")
@RequiredArgsConstructor
public class ReacaoController {

    private final ReacaoService reacaoService;
    private final ProfissionalService profissionalService;

    // ---------- POSTS ----------

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId, Principal principal) {
        Long userId = profissionalService.buscarIdPorEmail(principal.getName());
        long total = reacaoService.likePost(userId, postId);
        return ResponseEntity.ok(Map.of(
                "postId", postId,
                "liked", true,
                "likeCount", total
        ));
    }

    @PostMapping("/posts/{postId}/deslike")
    public ResponseEntity<?> deslikePost(@PathVariable Long postId, Principal principal) {
        Long userId = profissionalService.buscarIdPorEmail(principal.getName());
        long total = reacaoService.deslikePost(userId, postId);
        return ResponseEntity.ok(Map.of(
                "postId", postId,
                "desliked", true,
                "deslikeCount", total
        ));
    }

    @PostMapping("/posts/{postId}/like/toggle")
    public ResponseEntity<?> alternarLikePost(@PathVariable Long postId, Principal principal) {
        Long userId = profissionalService.buscarIdPorEmail(principal.getName());
        long total = reacaoService.alternarLikePost(userId, postId);
        return ResponseEntity.ok(Map.of(
                "postId", postId,
                "likeCount", total
        ));
    }

    @PostMapping("/posts/{postId}/deslike/toggle")
    public ResponseEntity<?> alternarDeslikePost(@PathVariable Long postId, Principal principal) {
        Long userId = profissionalService.buscarIdPorEmail(principal.getName());
        long total = reacaoService.alternarDeslikePost(userId, postId);
        return ResponseEntity.ok(Map.of(
                "postId", postId,
                "deslikeCount", total
        ));
    }

    // ---------- COMENTÁRIOS ----------

    @PostMapping("/comentarios/{comentarioId}/like")
    public ResponseEntity<?> likeComentario(@PathVariable Long comentarioId, Principal principal) {
        Long userId = profissionalService.buscarIdPorEmail(principal.getName());
        long total = reacaoService.likeComentario(userId, comentarioId);
        return ResponseEntity.ok(Map.of(
                "comentarioId", comentarioId,
                "liked", true,
                "likeCount", total
        ));
    }

    @PostMapping("/comentarios/{comentarioId}/deslike")
    public ResponseEntity<?> deslikeComentario(@PathVariable Long comentarioId, Principal principal) {
        Long userId = profissionalService.buscarIdPorEmail(principal.getName());
        long total = reacaoService.deslikeComentario(userId, comentarioId);
        return ResponseEntity.ok(Map.of(
                "comentarioId", comentarioId,
                "desliked", true,
                "deslikeCount", total
        ));
    }

    @PostMapping("/comentarios/{comentarioId}/like/toggle")
    public ResponseEntity<?> alternarLikeComentario(@PathVariable Long comentarioId, Principal principal) {
        Long userId = profissionalService.buscarIdPorEmail(principal.getName());
        long total = reacaoService.alternarLikeComentario(userId, comentarioId);
        return ResponseEntity.ok(Map.of(
                "comentarioId", comentarioId,
                "likeCount", total
        ));
    }

    @PostMapping("/comentarios/{comentarioId}/deslike/toggle")
    public ResponseEntity<?> alternarDeslikeComentario(@PathVariable Long comentarioId, Principal principal) {
        Long userId = profissionalService.buscarIdPorEmail(principal.getName());
        long total = reacaoService.alternarDeslikeComentario(userId, comentarioId);
        return ResponseEntity.ok(Map.of(
                "comentarioId", comentarioId,
                "deslikeCount", total
        ));
    }
}

