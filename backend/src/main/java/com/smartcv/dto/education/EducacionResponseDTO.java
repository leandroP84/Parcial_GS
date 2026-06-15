package com.smartcv.dto.education;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducacionResponseDTO {
    private Long id;
    private String institucion;
    private String carrera;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
