package com.smartcv.mapper;

import com.smartcv.dto.skill.HabilidadRequestDTO;
import com.smartcv.dto.skill.HabilidadResponseDTO;
import com.smartcv.entity.Habilidad;
import com.smartcv.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class HabilidadMapper {

    public Habilidad toEntity(HabilidadRequestDTO dto, Usuario usuario) {
        return Habilidad.builder()
                .usuario(usuario)
                .nombre(dto.getNombre())
                .nivel(dto.getNivel())
                .build();
    }

    public void updateEntity(Habilidad entity, HabilidadRequestDTO dto) {
        entity.setNombre(dto.getNombre());
        entity.setNivel(dto.getNivel());
    }

    public HabilidadResponseDTO toResponse(Habilidad entity) {
        return HabilidadResponseDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .nivel(entity.getNivel())
                .build();
    }
}
