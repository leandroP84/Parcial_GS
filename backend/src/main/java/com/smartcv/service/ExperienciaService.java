package com.smartcv.service;

import com.smartcv.dto.experience.ExperienciaRequestDTO;
import com.smartcv.dto.experience.ExperienciaResponseDTO;
import com.smartcv.entity.ExperienciaLaboral;
import com.smartcv.entity.Usuario;
import com.smartcv.exception.ResourceNotFoundException;
import com.smartcv.mapper.ExperienciaMapper;
import com.smartcv.util.DateRangeValidator;
import com.smartcv.repository.ExperienciaLaboralRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienciaService {

    private final ExperienciaLaboralRepository experienciaRepository;
    private final ExperienciaMapper experienciaMapper;
    private final CurrentUserService currentUserService;

    public List<ExperienciaResponseDTO> findAll() {
        Usuario usuario = currentUserService.getCurrentUsuario();
        return experienciaRepository.findByUsuarioIdOrderByFechaInicioDesc(usuario.getId())
                .stream()
                .map(experienciaMapper::toResponse)
                .toList();
    }

    public ExperienciaResponseDTO findById(Long id) {
        Usuario usuario = currentUserService.getCurrentUsuario();
        ExperienciaLaboral experiencia = experienciaRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Experiencia no encontrada"));
        return experienciaMapper.toResponse(experiencia);
    }

    @Transactional
    public ExperienciaResponseDTO create(ExperienciaRequestDTO request) {
        DateRangeValidator.validate(request.getFechaInicio(), request.getFechaFin(), "experiencia laboral");
        Usuario usuario = currentUserService.getCurrentUsuario();
        ExperienciaLaboral experiencia = experienciaMapper.toEntity(request, usuario);
        return experienciaMapper.toResponse(experienciaRepository.save(experiencia));
    }

    @Transactional
    public ExperienciaResponseDTO update(Long id, ExperienciaRequestDTO request) {
        DateRangeValidator.validate(request.getFechaInicio(), request.getFechaFin(), "experiencia laboral");
        Usuario usuario = currentUserService.getCurrentUsuario();
        ExperienciaLaboral experiencia = experienciaRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Experiencia no encontrada"));
        experienciaMapper.updateEntity(experiencia, request);
        return experienciaMapper.toResponse(experienciaRepository.save(experiencia));
    }

    @Transactional
    public void delete(Long id) {
        Usuario usuario = currentUserService.getCurrentUsuario();
        ExperienciaLaboral experiencia = experienciaRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Experiencia no encontrada"));
        experienciaRepository.delete(experiencia);
    }
}
