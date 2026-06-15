package com.smartcv.service;

import com.smartcv.dto.dashboard.DashboardStatsDTO;
import com.smartcv.entity.Usuario;
import com.smartcv.repository.EducacionRepository;
import com.smartcv.repository.ExperienciaLaboralRepository;
import com.smartcv.repository.HabilidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ExperienciaLaboralRepository experienciaRepository;
    private final EducacionRepository educacionRepository;
    private final HabilidadRepository habilidadRepository;
    private final CurrentUserService currentUserService;

    public DashboardStatsDTO getStats() {
        Usuario usuario = currentUserService.getCurrentUsuario();
        Long userId = usuario.getId();

        return DashboardStatsDTO.builder()
                .cantidadExperiencias(experienciaRepository.countByUsuarioId(userId))
                .cantidadEstudios(educacionRepository.countByUsuarioId(userId))
                .cantidadHabilidades(habilidadRepository.countByUsuarioId(userId))
                .build();
    }
}
