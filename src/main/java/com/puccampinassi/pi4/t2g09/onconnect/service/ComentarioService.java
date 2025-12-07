// Vitor

package com.puccampinassi.pi4.t2g09.onconnect.service;

import com.puccampinassi.pi4.t2g09.onconnect.dto.ComentarioDto;
import com.puccampinassi.pi4.t2g09.onconnect.dto.ProfissionalDto;
import com.puccampinassi.pi4.t2g09.onconnect.model.Comentario;
import com.puccampinassi.pi4.t2g09.onconnect.model.Post;
import com.puccampinassi.pi4.t2g09.onconnect.model.Profissional;
import com.puccampinassi.pi4.t2g09.onconnect.repository.ComentarioRepository;
import com.puccampinassi.pi4.t2g09.onconnect.repository.PostRepository;
import com.puccampinassi.pi4.t2g09.onconnect.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PostRepository postRepository;
    private final ProfissionalRepository profissionalRepository;

    // Listar comentários de um post
    public List<ComentarioDto> listarPorPost(Long postId) {
        List<Comentario> comentarios = comentarioRepository.findByPostPrincipalId(postId);
        return comentarios.stream()
                .map(this::toDTO)
                .toList();
    }

    // Criar comentário
    public ComentarioDto criarComentario(Long postId, Long autorId, String texto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado: " + postId));

        Profissional autor = profissionalRepository.findById(autorId)
                .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado: " + autorId));

        Comentario comentario = new Comentario();
        comentario.setPostPrincipal(post);
        comentario.setAutor(autor);
        comentario.setTexto(texto);

        // se não tiver @PrePersist, garante aqui
        comentario.setCreatedAt(LocalDateTime.now());

        Comentario salvo = comentarioRepository.save(comentario);
        return toDTO(salvo);
    }

    // conversão entidade -> DTO
    private ComentarioDto toDTO(Comentario c) {
        ProfissionalDto autorDTO = null;
        if (c.getAutor() != null) {
            autorDTO = new ProfissionalDto(
                    c.getAutor().getId(),
                    c.getAutor().getNomeCompleto()
            );
        }

        return new ComentarioDto(
                c.getId(),
                c.getTexto(),
                c.getCreatedAt(),
                c.getAutor() != null ? c.getAutor().getId() : null,
                autorDTO
        );
    }
}
