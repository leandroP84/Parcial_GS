package com.smartcv.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long cantidadExperiencias;
    private long cantidadEstudios;
    private long cantidadHabilidades;
}
