package com.smartcv.repository;

import com.smartcv.entity.PerfilProfesional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilProfesionalRepository extends JpaRepository<PerfilProfesional, Long> {
    Optional<PerfilProfesional> findByUsuarioId(Long usuarioId);
}
