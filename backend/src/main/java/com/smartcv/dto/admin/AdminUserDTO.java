package com.smartcv.dto.admin;

import com.smartcv.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Role rol;
}
