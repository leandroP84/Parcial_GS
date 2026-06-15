package com.smartcv.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAnalysisResponseDTO {
    private List<String> fortalezas;
    private List<String> debilidades;
    private List<String> recomendaciones;
    private List<String> habilidadesSugeridas;
}
