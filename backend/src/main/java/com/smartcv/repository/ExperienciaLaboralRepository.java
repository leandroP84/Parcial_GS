package com.smartcv.repository;

import com.smartcv.entity.ExperienciaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExperienciaLaboralRepository extends JpaRepository<ExperienciaLaboral, Long> {
    List<ExperienciaLaboral> findByUsuarioIdOrderByFechaInicioDesc(Long usuarioId);
    Optional<ExperienciaLaboral> findByIdAndUsuarioId(Long id, Long usuarioId);
    long countByUsuarioId(Long usuarioId);
}
