package com.puccampinassi.pi4.t2g09.onconnect.service;

import com.puccampinassi.pi4.t2g09.onconnect.model.Post;
import com.puccampinassi.pi4.t2g09.onconnect.model.reacao.DeslikeComentario;
import com.puccampinassi.pi4.t2g09.onconnect.model.reacao.DeslikePost;
import com.puccampinassi.pi4.t2g09.onconnect.model.reacao.LikeComentario;
import com.puccampinassi.pi4.t2g09.onconnect.model.reacao.LikePost;
import com.puccampinassi.pi4.t2g09.onconnect.repository.PostRepository;
import com.puccampinassi.pi4.t2g09.onconnect.repository.reacao.DeslikeComentarioRepository;
import com.puccampinassi.pi4.t2g09.onconnect.repository.reacao.DeslikePostRepository;
import com.puccampinassi.pi4.t2g09.onconnect.repository.reacao.LikeComentarioRepository;
import com.puccampinassi.pi4.t2g09.onconnect.repository.reacao.LikePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regras de negócio para reações.
 * Exclusão mútua: like e deslike não coexistem para o mesmo userId e origem.
 * Idempotência: repetir a mesma reação não cria duplicados.
 */
@Service
@RequiredArgsConstructor
public class ReacaoService {

    private final LikePostRepository likePostRepo;
    private final DeslikePostRepository deslikePostRepo;
    private final LikeComentarioRepository likeComentarioRepo;
    private final DeslikeComentarioRepository deslikeComentarioRepo;
    private final PostRepository postagemRepo;
                         

    // ---------- POSTAGEM ----------

    @Transactional
    public long likePost(Long userId, Long postId) {
        // Remove deslike existente antes de aplicar o like
        deslikePostRepo.deleteByIdUsuarioAndIdOrigem(userId, postId);

        if (!likePostRepo.existsByIdUsuarioAndIdOrigem(userId, postId)) {
            LikePost lp = new LikePost();
            lp.setIdUsuario(userId);
            lp.setIdOrigem(postId);
            likePostRepo.save(lp);
        }
        return likePostRepo.countByIdOrigem(postId);
    }

    @Transactional
    public long deslikePost(Long userId, Long postId) {
        // Remove like existente antes de aplicar o deslike
        likePostRepo.deleteByIdUsuarioAndIdOrigem(userId, postId);

        if (!deslikePostRepo.existsByIdUsuarioAndIdOrigem(userId, postId)) {
            DeslikePost dp = new DeslikePost();
            dp.setIdUsuario(userId);
            dp.setIdOrigem(postId);
            deslikePostRepo.save(dp);
        }
        return deslikePostRepo.countByIdOrigem(postId);
    }

    @Transactional(readOnly = true)
    public long contarLikesPost(Long postId) {
        return likePostRepo.countByIdOrigem(postId);
    }

    @Transactional(readOnly = true)
    public long contarDeslikesPost(Long postId) {
        return deslikePostRepo.countByIdOrigem(postId);
    }

    // ---------- COMENTÁRIO ----------

    @Transactional
    public long likeComentario(Long userId, Long comentarioId) {
        deslikeComentarioRepo.deleteByIdUsuarioAndIdOrigem(userId, comentarioId);

        if (!likeComentarioRepo.existsByIdUsuarioAndIdOrigem(userId, comentarioId)) {
            LikeComentario lc = new LikeComentario();
            lc.setIdUsuario(userId);
            lc.setIdOrigem(comentarioId);
            likeComentarioRepo.save(lc);
        }
        return likeComentarioRepo.countByIdOrigem(comentarioId);
    }

    @Transactional
    public long deslikeComentario(Long userId, Long comentarioId) {
        likeComentarioRepo.deleteByIdUsuarioAndIdOrigem(userId, comentarioId);

        if (!deslikeComentarioRepo.existsByIdUsuarioAndIdOrigem(userId, comentarioId)) {
            DeslikeComentario dc = new DeslikeComentario();
            dc.setIdUsuario(userId);
            dc.setIdOrigem(comentarioId);
            deslikeComentarioRepo.save(dc);
        }
        return deslikeComentarioRepo.countByIdOrigem(comentarioId);
    }

    @Transactional(readOnly = true)
    public long contarLikesComentario(Long comentarioId) {
        return likeComentarioRepo.countByIdOrigem(comentarioId);
    }

    @Transactional(readOnly = true)
    public long contarDeslikesComentario(Long comentarioId) {
        return deslikeComentarioRepo.countByIdOrigem(comentarioId);
    }


    /**
     * Toggle de like de post.
     * Se já existir like, remove. Se não existir, aplica.
     * Retorna a contagem final de likes do post.
     */
    @Transactional
    public long alternarLikePost(Long userId, Long postId) {
        if (likePostRepo.existsByIdUsuarioAndIdOrigem(userId, postId)) {
            likePostRepo.deleteByIdUsuarioAndIdOrigem(userId, postId);
        } else {
            // garantir exclusão mútua
            deslikePostRepo.deleteByIdUsuarioAndIdOrigem(userId, postId);
            LikePost lp = new LikePost();
            lp.setIdUsuario(userId);
            lp.setIdOrigem(postId);
            likePostRepo.save(lp);
        }
        atualizarContadoresPost(postId);

        return likePostRepo.countByIdOrigem(postId);
    }

    /**
     * Toggle de deslike de post.
     */
    @Transactional
    public long alternarDeslikePost(Long userId, Long postId) {
        if (deslikePostRepo.existsByIdUsuarioAndIdOrigem(userId, postId)) {
            deslikePostRepo.deleteByIdUsuarioAndIdOrigem(userId, postId);
        } else {
            likePostRepo.deleteByIdUsuarioAndIdOrigem(userId, postId);
            DeslikePost dp = new DeslikePost();
            dp.setIdUsuario(userId);
            dp.setIdOrigem(postId);
            deslikePostRepo.save(dp);
        }
        atualizarContadoresPost(postId);

        return deslikePostRepo.countByIdOrigem(postId);
    }

        /**
     * Toggle de like de comentário.
     * Se já existir like, remove. Se não existir, aplica.
     * Retorna a contagem final de likes do comentário.
     */
    @Transactional
    public long alternarLikeComentario(Long userId, Long comentarioId) {
        if (likeComentarioRepo.existsByIdUsuarioAndIdOrigem(userId, comentarioId)) {
            // Se já existe like, remove (desfazer like)
            likeComentarioRepo.deleteByIdUsuarioAndIdOrigem(userId, comentarioId);
        } else {
            // Remove eventual deslike existente antes de aplicar o like
            deslikeComentarioRepo.deleteByIdUsuarioAndIdOrigem(userId, comentarioId);

            LikeComentario lc = new LikeComentario();
            lc.setIdUsuario(userId);
            lc.setIdOrigem(comentarioId);
            likeComentarioRepo.save(lc);
        }
        return likeComentarioRepo.countByIdOrigem(comentarioId);
    }

    /**
     * Toggle de deslike de comentário.
     * Se já existir deslike, remove. Se não existir, aplica.
     * Retorna a contagem final de deslikes do comentário.
     */
    @Transactional
    public long alternarDeslikeComentario(Long userId, Long comentarioId) {
        if (deslikeComentarioRepo.existsByIdUsuarioAndIdOrigem(userId, comentarioId)) {
            // Se já existe deslike, remove (desfazer deslike)
            deslikeComentarioRepo.deleteByIdUsuarioAndIdOrigem(userId, comentarioId);
        } else {
            // Remove eventual like antes de aplicar o deslike
            likeComentarioRepo.deleteByIdUsuarioAndIdOrigem(userId, comentarioId);

            DeslikeComentario dc = new DeslikeComentario();
            dc.setIdUsuario(userId);
            dc.setIdOrigem(comentarioId);
            deslikeComentarioRepo.save(dc);
        }
        return deslikeComentarioRepo.countByIdOrigem(comentarioId);
    }

    @Transactional
    protected void atualizarContadoresPost(Long postId) {
        long likes = likePostRepo.countByIdOrigem(postId);
        long dislikes = deslikePostRepo.countByIdOrigem(postId);

        Post post = postagemRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado: " + postId));

        post.setQtdLikes((int) likes);
        post.setQtdDislikes((int) dislikes);

        postagemRepo.save(post);
    }



}
