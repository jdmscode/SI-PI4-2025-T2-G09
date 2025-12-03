package com.puccampinassi.pi4.t2g09.onconnect.repository;

import com.puccampinassi.pi4.t2g09.onconnect.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByPostPrincipalId(Long postId);
}
