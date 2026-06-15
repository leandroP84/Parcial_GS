package com.smartcv.mapper;

import com.smartcv.dto.education.EducacionRequestDTO;
import com.smartcv.dto.education.EducacionResponseDTO;
import com.smartcv.entity.Educacion;
import com.smartcv.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class EducacionMapper {

    public Educacion toEntity(EducacionRequestDTO dto, Usuario usuario) {
        return Educacion.builder()
                .usuario(usuario)
                .institucion(dto.getInstitucion())
                .carrera(dto.getCarrera())
                .fechaInicio(dto.getFechaInicio())
                .fechaFin(dto.getFechaFin())
                .build();
    }

    public void updateEntity(Educacion entity, EducacionRequestDTO dto) {
        entity.setInstitucion(dto.getInstitucion());
        entity.setCarrera(dto.getCarrera());
        entity.setFechaInicio(dto.getFechaInicio());
        entity.setFechaFin(dto.getFechaFin());
    }

    public EducacionResponseDTO toResponse(Educacion entity) {
        return EducacionResponseDTO.builder()
                .id(entity.getId())
                .institucion(entity.getInstitucion())
                .carrera(entity.getCarrera())
                .fechaInicio(entity.getFechaInicio())
                .fechaFin(entity.getFechaFin())
                .build();
    }
}
