package com.smartcv.controller;

import com.smartcv.dto.skill.HabilidadRequestDTO;
import com.smartcv.dto.skill.HabilidadResponseDTO;
import com.smartcv.service.HabilidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habilidades")
@RequiredArgsConstructor
public class HabilidadController {

    private final HabilidadService habilidadService;

    @GetMapping
    public ResponseEntity<List<HabilidadResponseDTO>> findAll() {
        return ResponseEntity.ok(habilidadService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabilidadResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(habilidadService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HabilidadResponseDTO> create(@Valid @RequestBody HabilidadRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habilidadService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabilidadResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody HabilidadRequestDTO request) {
        return ResponseEntity.ok(habilidadService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        habilidadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
