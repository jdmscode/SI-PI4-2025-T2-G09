package com.puccampinassi.pi4.t2g09.onconnect.service;

import com.puccampinassi.pi4.t2g09.onconnect.model.Comentario;
import com.puccampinassi.pi4.t2g09.onconnect.repository.ComentarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;

    public Comentario salvar(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    public List<Comentario> listarPorPost(Long postId) {
        return comentarioRepository.findByPostPrincipalId(postId);
    }
}
