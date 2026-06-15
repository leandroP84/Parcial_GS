package com.smartcv.controller;

import com.smartcv.dto.profile.PerfilRequestDTO;
import com.smartcv.dto.profile.PerfilResponseDTO;
import com.smartcv.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping
    public ResponseEntity<PerfilResponseDTO> getPerfil() {
        return ResponseEntity.ok(perfilService.getPerfil());
    }

    @PutMapping
    public ResponseEntity<PerfilResponseDTO> savePerfil(@Valid @RequestBody PerfilRequestDTO request) {
        return ResponseEntity.ok(perfilService.savePerfil(request));
    }
}
