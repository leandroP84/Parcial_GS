package com.smartcv.mapper;

import com.smartcv.dto.experience.ExperienciaRequestDTO;
import com.smartcv.dto.experience.ExperienciaResponseDTO;
import com.smartcv.entity.ExperienciaLaboral;
import com.smartcv.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ExperienciaMapper {

    public ExperienciaLaboral toEntity(ExperienciaRequestDTO dto, Usuario usuario) {
        return ExperienciaLaboral.builder()
                .usuario(usuario)
                .empresa(dto.getEmpresa())
                .cargo(dto.getCargo())
                .fechaInicio(dto.getFechaInicio())
                .fechaFin(dto.getFechaFin())
                .descripcion(dto.getDescripcion())
                .build();
    }

    public void updateEntity(ExperienciaLaboral entity, ExperienciaRequestDTO dto) {
        entity.setEmpresa(dto.getEmpresa());
        entity.setCargo(dto.getCargo());
        entity.setFechaInicio(dto.getFechaInicio());
        entity.setFechaFin(dto.getFechaFin());
        entity.setDescripcion(dto.getDescripcion());
    }

    public ExperienciaResponseDTO toResponse(ExperienciaLaboral entity) {
        return ExperienciaResponseDTO.builder()
                .id(entity.getId())
                .empresa(entity.getEmpresa())
                .cargo(entity.getCargo())
                .fechaInicio(entity.getFechaInicio())
                .fechaFin(entity.getFechaFin())
                .descripcion(entity.getDescripcion())
                .build();
    }
}
