package com.smartcv.controller;

import com.smartcv.dto.ai.AiAnalysisResponseDTO;
import com.smartcv.dto.ai.CoverLetterRequestDTO;
import com.smartcv.dto.ai.CoverLetterResponseDTO;
import com.smartcv.service.GeminiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final GeminiService geminiService;

    @PostMapping("/analyze-cv")
    public ResponseEntity<AiAnalysisResponseDTO> analyzeCv() {
        return ResponseEntity.ok(geminiService.analyzeCv());
    }

    @PostMapping("/generate-cover-letter")
    public ResponseEntity<CoverLetterResponseDTO> generateCoverLetter(
            @Valid @RequestBody(required = false) CoverLetterRequestDTO request) {
        if (request == null) {
            request = new CoverLetterRequestDTO();
        }
        return ResponseEntity.ok(geminiService.generateCoverLetter(request));
    }
}
