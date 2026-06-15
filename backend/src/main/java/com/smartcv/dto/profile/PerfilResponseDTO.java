package com.smartcv.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String resumenProfesional;
    private String telefono;
    private String linkedin;
    private String github;
}
