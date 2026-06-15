package com.smartcv.service;

import com.smartcv.dto.admin.AdminUserDTO;
import com.smartcv.entity.Usuario;
import com.smartcv.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<AdminUserDTO> listUsers() {
        return usuarioRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private AdminUserDTO toDto(Usuario usuario) {
        return AdminUserDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }
}
