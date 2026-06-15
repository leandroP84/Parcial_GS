package com.smartcv.service;

import com.smartcv.dto.skill.HabilidadRequestDTO;
import com.smartcv.dto.skill.HabilidadResponseDTO;
import com.smartcv.entity.Habilidad;
import com.smartcv.entity.Usuario;
import com.smartcv.exception.ResourceNotFoundException;
import com.smartcv.mapper.HabilidadMapper;
import com.smartcv.repository.HabilidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabilidadService {

    private final HabilidadRepository habilidadRepository;
    private final HabilidadMapper habilidadMapper;
    private final CurrentUserService currentUserService;

    public List<HabilidadResponseDTO> findAll() {
        Usuario usuario = currentUserService.getCurrentUsuario();
        return habilidadRepository.findByUsuarioIdOrderByNombreAsc(usuario.getId())
                .stream()
                .map(habilidadMapper::toResponse)
                .toList();
    }

    public HabilidadResponseDTO findById(Long id) {
        Usuario usuario = currentUserService.getCurrentUsuario();
        Habilidad habilidad = habilidadRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habilidad no encontrada"));
        return habilidadMapper.toResponse(habilidad);
    }

    @Transactional
    public HabilidadResponseDTO create(HabilidadRequestDTO request) {
        Usuario usuario = currentUserService.getCurrentUsuario();
        Habilidad habilidad = habilidadMapper.toEntity(request, usuario);
        return habilidadMapper.toResponse(habilidadRepository.save(habilidad));
    }

    @Transactional
    public HabilidadResponseDTO update(Long id, HabilidadRequestDTO request) {
        Usuario usuario = currentUserService.getCurrentUsuario();
        Habilidad habilidad = habilidadRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habilidad no encontrada"));
        habilidadMapper.updateEntity(habilidad, request);
        return habilidadMapper.toResponse(habilidadRepository.save(habilidad));
    }

    @Transactional
    public void delete(Long id) {
        Usuario usuario = currentUserService.getCurrentUsuario();
        Habilidad habilidad = habilidadRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Habilidad no encontrada"));
        habilidadRepository.delete(habilidad);
    }
}
