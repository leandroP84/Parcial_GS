package com.smartcv.service;

import com.smartcv.dto.profile.PerfilRequestDTO;
import com.smartcv.dto.profile.PerfilResponseDTO;
import com.smartcv.entity.PerfilProfesional;
import com.smartcv.entity.Usuario;
import com.smartcv.mapper.PerfilMapper;
import com.smartcv.repository.PerfilProfesionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilProfesionalRepository perfilRepository;
    private final PerfilMapper perfilMapper;
    private final CurrentUserService currentUserService;

    @Transactional(readOnly = true)
    public PerfilResponseDTO getPerfil() {
        Usuario usuario = currentUserService.getCurrentUsuario();
        PerfilProfesional perfil = perfilRepository.findByUsuarioId(usuario.getId()).orElse(null);
        return perfilMapper.toResponse(usuario, perfil);
    }

    @Transactional
    public PerfilResponseDTO savePerfil(PerfilRequestDTO request) {
        Usuario usuario = currentUserService.getCurrentUsuario();
        PerfilProfesional perfil = perfilRepository.findByUsuarioId(usuario.getId()).orElse(null);

        if (perfil == null) {
            perfil = perfilMapper.toEntity(request, usuario);
            perfilRepository.save(perfil);
        } else {
            perfilMapper.updateEntity(perfil, request);
            perfilRepository.save(perfil);
        }

        return perfilMapper.toResponse(usuario, perfil);
    }
}
