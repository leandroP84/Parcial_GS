package com.smartcv.repository;

import com.smartcv.entity.Educacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EducacionRepository extends JpaRepository<Educacion, Long> {
    List<Educacion> findByUsuarioIdOrderByFechaInicioDesc(Long usuarioId);
    Optional<Educacion> findByIdAndUsuarioId(Long id, Long usuarioId);
    long countByUsuarioId(Long usuarioId);
}
