package com.puccampinassi.pi4.t2g09.onconnect.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.puccampinassi.pi4.t2g09.onconnect.model.Profissional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Optional<Profissional> findByEmail(String email);
    boolean existsByRegistroProfissional(String registroProfissional);
}
