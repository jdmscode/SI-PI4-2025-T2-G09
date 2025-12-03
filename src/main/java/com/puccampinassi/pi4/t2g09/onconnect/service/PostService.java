package com.puccampinassi.pi4.t2g09.onconnect.service;

import com.puccampinassi.pi4.t2g09.onconnect.model.Post;
import com.puccampinassi.pi4.t2g09.onconnect.model.Profissional;
import com.puccampinassi.pi4.t2g09.onconnect.repository.PostRepository;
import com.puccampinassi.pi4.t2g09.onconnect.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final ProfissionalRepository profissionalRepository;

    public Post criar(Post post) {
        // pega o "username" do usuário autenticado (o email)
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Profissional autor = profissionalRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profissional autenticado não encontrado"));

        // garante que é um novo post
        post.setId(null);

        // zera contadores de reação na criação
        post.setQtdLikes(0);
        post.setQtdDislikes(0);

        // associa o autor logado
        post.setAutor(autor);

        // createdAt é preenchido pelo @PrePersist
        return repository.save(post);
    }

    public List<Post> listar() {
        return repository.findAll();
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
