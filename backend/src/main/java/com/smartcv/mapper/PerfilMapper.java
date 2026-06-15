package com.smartcv.mapper;

import com.smartcv.dto.profile.PerfilRequestDTO;
import com.smartcv.dto.profile.PerfilResponseDTO;
import com.smartcv.entity.PerfilProfesional;
import com.smartcv.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {

    public PerfilProfesional toEntity(PerfilRequestDTO dto, Usuario usuario) {
        PerfilProfesional perfil = PerfilProfesional.builder()
                .usuario(usuario)
                .resumenProfesional(dto.getResumenProfesional())
                .telefono(dto.getTelefono())
                .linkedin(dto.getLinkedin())
                .github(dto.getGithub())
                .build();
        usuario.setPerfilProfesional(perfil);
        return perfil;
    }

    public void updateEntity(PerfilProfesional entity, PerfilRequestDTO dto) {
        entity.setResumenProfesional(dto.getResumenProfesional());
        entity.setTelefono(dto.getTelefono());
        entity.setLinkedin(dto.getLinkedin());
        entity.setGithub(dto.getGithub());
    }

    public PerfilResponseDTO toResponse(Usuario usuario, PerfilProfesional perfil) {
        PerfilResponseDTO.PerfilResponseDTOBuilder builder = PerfilResponseDTO.builder()
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail());

        if (perfil != null) {
            builder.id(perfil.getId())
                    .resumenProfesional(perfil.getResumenProfesional())
                    .telefono(perfil.getTelefono())
                    .linkedin(perfil.getLinkedin())
                    .github(perfil.getGithub());
        }

        return builder.build();
    }
}
