package com.smartcv.controller;

import com.smartcv.dto.experience.ExperienciaRequestDTO;
import com.smartcv.dto.experience.ExperienciaResponseDTO;
import com.smartcv.service.ExperienciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiencias")
@RequiredArgsConstructor
public class ExperienciaController {

    private final ExperienciaService experienciaService;

    @GetMapping
    public ResponseEntity<List<ExperienciaResponseDTO>> findAll() {
        return ResponseEntity.ok(experienciaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienciaResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(experienciaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ExperienciaResponseDTO> create(@Valid @RequestBody ExperienciaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(experienciaService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienciaResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody ExperienciaRequestDTO request) {
        return ResponseEntity.ok(experienciaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        experienciaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
