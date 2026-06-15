package com.smartcv.service;

import com.smartcv.dto.education.EducacionRequestDTO;
import com.smartcv.dto.education.EducacionResponseDTO;
import com.smartcv.entity.Educacion;
import com.smartcv.entity.Usuario;
import com.smartcv.exception.ResourceNotFoundException;
import com.smartcv.mapper.EducacionMapper;
import com.smartcv.util.DateRangeValidator;
import com.smartcv.repository.EducacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducacionService {

    private final EducacionRepository educacionRepository;
    private final EducacionMapper educacionMapper;
    private final CurrentUserService currentUserService;

    public List<EducacionResponseDTO> findAll() {
        Usuario usuario = currentUserService.getCurrentUsuario();
        return educacionRepository.findByUsuarioIdOrderByFechaInicioDesc(usuario.getId())
                .stream()
                .map(educacionMapper::toResponse)
                .toList();
    }

    public EducacionResponseDTO findById(Long id) {
        Usuario usuario = currentUserService.getCurrentUsuario();
        Educacion educacion = educacionRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Educación no encontrada"));
        return educacionMapper.toResponse(educacion);
    }

    @Transactional
    public EducacionResponseDTO create(EducacionRequestDTO request) {
        DateRangeValidator.validate(request.getFechaInicio(), request.getFechaFin(), "educación");
        Usuario usuario = currentUserService.getCurrentUsuario();
        Educacion educacion = educacionMapper.toEntity(request, usuario);
        return educacionMapper.toResponse(educacionRepository.save(educacion));
    }

    @Transactional
    public EducacionResponseDTO update(Long id, EducacionRequestDTO request) {
        DateRangeValidator.validate(request.getFechaInicio(), request.getFechaFin(), "educación");
        Usuario usuario = currentUserService.getCurrentUsuario();
        Educacion educacion = educacionRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Educación no encontrada"));
        educacionMapper.updateEntity(educacion, request);
        return educacionMapper.toResponse(educacionRepository.save(educacion));
    }

    @Transactional
    public void delete(Long id) {
        Usuario usuario = currentUserService.getCurrentUsuario();
        Educacion educacion = educacionRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Educación no encontrada"));
        educacionRepository.delete(educacion);
    }
}
