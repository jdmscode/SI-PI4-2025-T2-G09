package com.puccampinassi.pi4.t2g09.onconnect.repository;

import com.puccampinassi.pi4.t2g09.onconnect.dto.PostResumoDto;
import com.puccampinassi.pi4.t2g09.onconnect.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        select new com.puccampinassi.pi4.t2g09.onconnect.dto.PostResumoDto(
            p.id,
            p.titulo,
            p.texto,
            p.qtdLikes,
            p.qtdDislikes,
            p.autor.id,
            p.autor.nomeCompleto,
            p.createdAt
        )
        from Post p
        order by
            case
                when p.qtdLikes >= :threshold
                     and p.createdAt >= :limitePopular
                then 1
                else 0
            end desc,
            p.qtdLikes desc,
            p.createdAt desc
        """)
    Page<PostResumoDto> listarFeed(
            @Param("threshold") int threshold,
            @Param("limitePopular") LocalDateTime limitePopular,
            Pageable pageable
    );

    @Query("""
        select new com.puccampinassi.pi4.t2g09.onconnect.dto.PostResumoDto(
            p.id,
            p.titulo,
            p.texto,
            p.qtdLikes,
            p.qtdDislikes,
            p.autor.id,
            p.autor.nomeCompleto,
            p.createdAt
        )
        from Post p
        where lower(p.titulo) like lower(concat('%', :termo, '%'))
        order by
            case
                when p.qtdLikes >= :threshold
                     and p.createdAt >= :limitePopular
                then 1
                else 0
            end desc,
            p.qtdLikes desc,
            p.createdAt desc
        """)
    Page<PostResumoDto> buscarNoFeedPorTitulo(
            @Param("termo") String termo,
            @Param("threshold") int threshold,
            @Param("limitePopular") LocalDateTime limitePopular,
            Pageable pageable
    );
}
