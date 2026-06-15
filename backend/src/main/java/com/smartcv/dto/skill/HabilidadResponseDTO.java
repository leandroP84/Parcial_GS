package com.smartcv.dto.skill;

import com.smartcv.entity.enums.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabilidadResponseDTO {
    private Long id;
    private String nombre;
    private SkillLevel nivel;
}
