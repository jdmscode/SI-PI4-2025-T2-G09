package com.puccampinassi.pi4.t2g09.onconnect.repository;

import com.puccampinassi.pi4.t2g09.onconnect.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
