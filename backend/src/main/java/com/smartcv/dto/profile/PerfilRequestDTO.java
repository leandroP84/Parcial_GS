package com.smartcv.dto.profile;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PerfilRequestDTO {

    @Size(max = 5000, message = "El resumen no puede superar 5000 caracteres")
    private String resumenProfesional;

    @Size(max = 30, message = "El teléfono no puede superar 30 caracteres")
    private String telefono;

    @Size(max = 500, message = "LinkedIn no puede superar 500 caracteres")
    private String linkedin;

    @Size(max = 500, message = "GitHub no puede superar 500 caracteres")
    private String github;
}
