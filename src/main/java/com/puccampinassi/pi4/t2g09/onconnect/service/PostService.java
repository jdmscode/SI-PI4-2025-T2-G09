package com.puccampinassi.pi4.t2g09.onconnect.service;

import com.puccampinassi.pi4.t2g09.onconnect.model.Post;
import com.puccampinassi.pi4.t2g09.onconnect.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repository;

    public Post criar(Post post) {
        return repository.save(post);
    }

    public List<Post> listar() {
        return repository.findAll();
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
