package com.puccampinassi.pi4.t2g09.onconnect.service;

import com.puccampinassi.pi4.t2g09.onconnect.model.Post;
import com.puccampinassi.pi4.t2g09.onconnect.model.Profissional;
import com.puccampinassi.pi4.t2g09.onconnect.repository.PostRepository;
import com.puccampinassi.pi4.t2g09.onconnect.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final ProfissionalRepository profissionalRepository;
    private final FileStorageService fileStorageService;

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

    public Post criar(String titulo, String texto, MultipartFile imagem) {
        // pega o email do usuário autenticado
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Profissional autor = profissionalRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profissional autenticado não encontrado"));

        Post post = new Post();
        post.setTitulo(titulo);
        post.setTexto(texto);
        post.setAutor(autor);
        post.setQtdLikes(0);
        post.setQtdDislikes(0);

        // salva imagem se tiver
        String imagemUrl = fileStorageService.salvarImagem(imagem);
        post.setImagemUrl(imagemUrl);

        return repository.save(post);
    }

    public List<Post> listar() {
        return repository.findAll();
    }
    
    public Optional<Post> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

}
