package com.puccampinassi.pi4.t2g09.onconnect.controller;

import com.puccampinassi.pi4.t2g09.onconnect.model.Post;
import com.puccampinassi.pi4.t2g09.onconnect.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> criar(@RequestBody Post post) {
        return ResponseEntity.ok(postService.criar(post));
    }

    @GetMapping
    public ResponseEntity<List<Post>> listar() {
        return ResponseEntity.ok(postService.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        postService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
