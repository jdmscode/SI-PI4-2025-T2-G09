package com.puccampinassi.pi4.t2g09.onconnect.service;

import com.puccampinassi.pi4.t2g09.onconnect.dto.PostResumoDto;
import com.puccampinassi.pi4.t2g09.onconnect.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;

    private static final int POPULAR_THRESHOLD = 10;
    private static final long DIAS_POPULAR = 3;

    public Page<PostResumoDto> listarFeed(int page, int size, String termoBusca) {
        Pageable pageable = PageRequest.of(page, size);

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime limitePopular = agora.minusDays(DIAS_POPULAR);

        boolean temBusca = termoBusca != null && !termoBusca.isBlank();

        if (temBusca) {
            return postRepository.buscarNoFeedPorTitulo(
                    termoBusca.trim(),
                    POPULAR_THRESHOLD,
                    limitePopular,
                    pageable
            );
        }

        return postRepository.listarFeed(
                POPULAR_THRESHOLD,
                limitePopular,
                pageable
        );
    }
}
