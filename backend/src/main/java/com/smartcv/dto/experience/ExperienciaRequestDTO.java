package com.smartcv.dto.experience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienciaRequestDTO {

    @NotBlank(message = "La empresa es obligatoria")
    @Size(max = 200, message = "La empresa no puede superar 200 caracteres")
    private String empresa;

    @NotBlank(message = "El cargo es obligatorio")
    @Size(max = 200, message = "El cargo no puede superar 200 caracteres")
    private String cargo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Size(max = 5000, message = "La descripción no puede superar 5000 caracteres")
    private String descripcion;
}
