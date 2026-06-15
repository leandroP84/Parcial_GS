package com.smartcv.dto.education;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EducacionRequestDTO {

    @NotBlank(message = "La institución es obligatoria")
    @Size(max = 200, message = "La institución no puede superar 200 caracteres")
    private String institucion;

    @NotBlank(message = "La carrera es obligatoria")
    @Size(max = 200, message = "La carrera no puede superar 200 caracteres")
    private String carrera;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}
