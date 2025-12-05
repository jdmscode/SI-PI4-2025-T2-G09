package com.puccampinassi.pi4.t2g09.onconnect.repository.reacao;

import com.puccampinassi.pi4.t2g09.onconnect.model.reacao.DeslikeComentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeslikeComentarioRepository extends JpaRepository<DeslikeComentario, Long> {
  boolean existsByIdUsuarioAndIdOrigem(Long idUsuario, Long idOrigem);
  long countByIdOrigem(Long idOrigem);
  void deleteByIdUsuarioAndIdOrigem(Long idUsuario, Long idOrigem);
}
