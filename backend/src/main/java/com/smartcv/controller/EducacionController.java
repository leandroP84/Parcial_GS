package com.smartcv.controller;

import com.smartcv.dto.education.EducacionRequestDTO;
import com.smartcv.dto.education.EducacionResponseDTO;
import com.smartcv.service.EducacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/educaciones")
@RequiredArgsConstructor
public class EducacionController {

    private final EducacionService educacionService;

    @GetMapping
    public ResponseEntity<List<EducacionResponseDTO>> findAll() {
        return ResponseEntity.ok(educacionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducacionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(educacionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EducacionResponseDTO> create(@Valid @RequestBody EducacionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(educacionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducacionResponseDTO> update(
            @PathVariable Long id, @Valid @RequestBody EducacionRequestDTO request) {
        return ResponseEntity.ok(educacionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        educacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
