package com.smartcv.dto.ai;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CoverLetterRequestDTO {

    @Size(max = 200, message = "El puesto objetivo no puede superar 200 caracteres")
    private String puestoObjetivo;

    @Size(max = 200, message = "La empresa objetivo no puede superar 200 caracteres")
    private String empresaObjetivo;
}
