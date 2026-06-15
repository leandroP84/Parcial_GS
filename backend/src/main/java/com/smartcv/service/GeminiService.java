package com.smartcv.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcv.dto.ai.AiAnalysisResponseDTO;
import com.smartcv.dto.ai.CoverLetterRequestDTO;
import com.smartcv.dto.ai.CoverLetterResponseDTO;
import com.smartcv.entity.*;
import com.smartcv.exception.GeminiApiException;
import com.smartcv.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CurrentUserService currentUserService;
    private final ExperienciaLaboralRepository experienciaRepository;
    private final EducacionRepository educacionRepository;
    private final HabilidadRepository habilidadRepository;
    private final PerfilProfesionalRepository perfilRepository;

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    /** gemini-2.0-flash tiene cuota free = 0; priorizar modelos con cuota activa. */
    private static final List<String> MODEL_FALLBACKS = List.of(
            "gemini-2.5-flash",
            "gemini-2.5-flash-lite",
            "gemini-1.5-flash-latest",
            "gemini-2.0-flash"
    );

    @Transactional(readOnly = true)
    public AiAnalysisResponseDTO analyzeCv() {
        String cvData = buildCvData();
        String prompt = """
                Eres un experto en recursos humanos y empleabilidad.
                Analiza el siguiente perfil profesional y responde ÚNICAMENTE con un JSON válido (sin markdown) con esta estructura exacta:
                {
                  "fortalezas": ["..."],
                  "debilidades": ["..."],
                  "recomendaciones": ["..."],
                  "habilidadesSugeridas": ["..."]
                }
                Cada array debe tener al menos 3 elementos. Responde en español.

                Perfil:
                """ + cvData;

        String response = callGemini(prompt, true);
        return parseAnalysisResponse(response);
    }

    @Transactional(readOnly = true)
    public CoverLetterResponseDTO generateCoverLetter(CoverLetterRequestDTO request) {
        String cvData = buildCvData();
        String puesto = request.getPuestoObjetivo() != null ? request.getPuestoObjetivo() : "el puesto al que aplica";
        String empresa = request.getEmpresaObjetivo() != null ? request.getEmpresaObjetivo() : "la empresa";

        String prompt = """
                Eres un redactor profesional de cartas de presentación.
                Genera una carta de presentación profesional en español, persuasiva y personalizada.
                Máximo 400 palabras. No uses markdown ni encabezados. Solo el texto de la carta.

                Puesto objetivo: %s
                Empresa objetivo: %s

                Perfil del candidato:
                %s
                """.formatted(puesto, empresa, cvData);

        String carta = callGemini(prompt, false);
        return CoverLetterResponseDTO.builder().carta(carta.trim()).build();
    }

    private String buildCvData() {
        Usuario usuario = currentUserService.getCurrentUsuario();
        PerfilProfesional perfil = perfilRepository.findByUsuarioId(usuario.getId()).orElse(null);

        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(usuario.getNombre()).append(" ").append(usuario.getApellido()).append("\n");
        sb.append("Email: ").append(usuario.getEmail()).append("\n");

        if (perfil != null) {
            if (perfil.getResumenProfesional() != null) {
                sb.append("Resumen: ").append(perfil.getResumenProfesional()).append("\n");
            }
            if (perfil.getTelefono() != null) sb.append("Teléfono: ").append(perfil.getTelefono()).append("\n");
            if (perfil.getLinkedin() != null) sb.append("LinkedIn: ").append(perfil.getLinkedin()).append("\n");
            if (perfil.getGithub() != null) sb.append("GitHub: ").append(perfil.getGithub()).append("\n");
        }

        List<ExperienciaLaboral> experiencias = experienciaRepository
                .findByUsuarioIdOrderByFechaInicioDesc(usuario.getId());
        if (!experiencias.isEmpty()) {
            sb.append("\nExperiencia Laboral:\n");
            for (ExperienciaLaboral exp : experiencias) {
                sb.append("- ").append(exp.getCargo()).append(" en ").append(exp.getEmpresa());
                sb.append(" (").append(exp.getFechaInicio()).append(" - ");
                sb.append(exp.getFechaFin() != null ? exp.getFechaFin() : "actual").append(")\n");
                if (exp.getDescripcion() != null) sb.append("  ").append(exp.getDescripcion()).append("\n");
            }
        }

        List<Educacion> educaciones = educacionRepository.findByUsuarioIdOrderByFechaInicioDesc(usuario.getId());
        if (!educaciones.isEmpty()) {
            sb.append("\nEducación:\n");
            for (Educacion edu : educaciones) {
                sb.append("- ").append(edu.getCarrera()).append(" en ").append(edu.getInstitucion());
                sb.append(" (").append(edu.getFechaInicio()).append(" - ");
                sb.append(edu.getFechaFin() != null ? edu.getFechaFin() : "en curso").append(")\n");
            }
        }

        List<Habilidad> habilidades = habilidadRepository.findByUsuarioIdOrderByNombreAsc(usuario.getId());
        if (!habilidades.isEmpty()) {
            sb.append("\nHabilidades:\n");
            for (Habilidad h : habilidades) {
                sb.append("- ").append(h.getNombre()).append(" (").append(h.getNivel()).append(")\n");
            }
        }

        return sb.toString();
    }

    private String callGemini(String prompt, boolean jsonResponse) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new GeminiApiException("La API key de Gemini no está configurada. Configure GEMINI_API_KEY.");
        }

        List<String> modelsToTry = Stream.concat(Stream.of(model), MODEL_FALLBACKS.stream())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        GeminiApiException lastError = null;

        for (String modelName : modelsToTry) {
            try {
                return invokeGemini(prompt, modelName, jsonResponse);
            } catch (GeminiApiException e) {
                lastError = e;
                String msg = e.getMessage() != null ? e.getMessage() : "";
                if (msg.contains("404") || (msg.contains("429") && msg.contains("limit: 0"))) {
                    continue;
                }
                throw e;
            }
        }

        throw lastError != null ? lastError : new GeminiApiException(
                "No se pudo conectar con Gemini. Configure GEMINI_MODEL=gemini-2.5-flash y verifique su API key en AI Studio.");
    }

    private String invokeGemini(String prompt, String modelName, boolean jsonResponse) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                + modelName + ":generateContent";

        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("maxOutputTokens", 2048);
        if (jsonResponse) {
            generationConfig.put("responseMimeType", "application/json");
            generationConfig.put("responseSchema", buildAnalysisResponseSchema());
        }

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of(
                        "parts", List.of(Map.of("text", prompt))
                )),
                "generationConfig", generationConfig
        );

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-goog-api-key", apiKey);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new GeminiApiException("Error al comunicarse con Gemini API (modelo: " + modelName + ")");
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode candidates = root.path("candidates");
            if (candidates.isEmpty()) {
                JsonNode blockReason = root.path("promptFeedback").path("blockReason");
                if (!blockReason.isMissingNode() && !blockReason.asText().isBlank()) {
                    throw new GeminiApiException("Gemini bloqueó la solicitud: " + blockReason.asText());
                }
                throw new GeminiApiException("Gemini no devolvió respuesta (modelo: " + modelName + ")");
            }

            JsonNode candidate = candidates.get(0);
            String finishReason = candidate.path("finishReason").asText("");
            if ("SAFETY".equals(finishReason)) {
                throw new GeminiApiException("Gemini filtró la respuesta por políticas de seguridad");
            }

            JsonNode parts = candidate.path("content").path("parts");
            if (!parts.isArray() || parts.isEmpty()) {
                throw new GeminiApiException("Gemini devolvió respuesta vacía (modelo: " + modelName + ")");
            }

            StringBuilder text = new StringBuilder();
            for (JsonNode part : parts) {
                if (part.has("text")) {
                    text.append(part.get("text").asText());
                }
            }
            if (text.isEmpty()) {
                throw new GeminiApiException("Gemini no devolvió texto en la respuesta");
            }
            return text.toString();
        } catch (GeminiApiException e) {
            throw e;
        } catch (HttpClientErrorException e) {
            String detail = extractGeminiError(e);
            throw new GeminiApiException("Gemini API error " + e.getStatusCode().value()
                    + " (modelo: " + modelName + "): " + detail);
        } catch (Exception e) {
            throw new GeminiApiException("Error al procesar respuesta de Gemini (modelo: " + modelName + "): "
                    + e.getMessage(), e);
        }
    }

    private String extractGeminiError(HttpClientErrorException e) {
        try {
            JsonNode error = objectMapper.readTree(e.getResponseBodyAsString()).path("error");
            if (!error.isMissingNode()) {
                return error.path("message").asText(e.getMessage());
            }
        } catch (Exception ignored) {
            // usar mensaje por defecto
        }
        return e.getMessage();
    }

    private Map<String, Object> buildAnalysisResponseSchema() {
        Map<String, Object> stringItem = Map.of("type", "STRING");
        Map<String, Object> stringArray = Map.of(
                "type", "ARRAY",
                "items", stringItem
        );
        Map<String, Object> properties = Map.of(
                "fortalezas", stringArray,
                "debilidades", stringArray,
                "recomendaciones", stringArray,
                "habilidadesSugeridas", stringArray
        );
        return Map.of(
                "type", "OBJECT",
                "properties", properties,
                "required", List.of("fortalezas", "debilidades", "recomendaciones", "habilidadesSugeridas")
        );
    }

    private AiAnalysisResponseDTO parseAnalysisResponse(String response) {
        try {
            String json = extractJsonBlock(response);
            JsonNode node = objectMapper.readTree(json);

            List<String> fortalezas = readStringList(node, "fortalezas", "strengths");
            List<String> debilidades = readStringList(node, "debilidades", "weaknesses");
            List<String> recomendaciones = readStringList(node, "recomendaciones", "recommendations");
            List<String> habilidades = readStringList(node, "habilidadesSugeridas", "suggestedSkills", "habilidades_sugeridas");

            if (fortalezas.isEmpty() && debilidades.isEmpty() && recomendaciones.isEmpty() && habilidades.isEmpty()) {
                // Buscar campos en JSON anidado (ej. { "analisis": { ... } })
                fortalezas = readStringListDeep(node, "fortalezas", "strengths");
                debilidades = readStringListDeep(node, "debilidades", "weaknesses");
                recomendaciones = readStringListDeep(node, "recomendaciones", "recommendations");
                habilidades = readStringListDeep(node, "habilidadesSugeridas", "suggestedSkills", "habilidades_sugeridas");
            }

            if (fortalezas.isEmpty() && debilidades.isEmpty() && recomendaciones.isEmpty() && habilidades.isEmpty()) {
                throw new IllegalArgumentException("JSON sin campos esperados: " + json.substring(0, Math.min(200, json.length())));
            }

            return AiAnalysisResponseDTO.builder()
                    .fortalezas(defaultList(fortalezas))
                    .debilidades(defaultList(debilidades))
                    .recomendaciones(defaultList(recomendaciones))
                    .habilidadesSugeridas(defaultList(habilidades))
                    .build();
        } catch (GeminiApiException e) {
            throw e;
        } catch (Exception e) {
            throw new GeminiApiException(
                    "No se pudo interpretar el análisis de IA. Intente nuevamente en unos segundos.", e);
        }
    }

    private List<String> defaultList(List<String> list) {
        return list != null && !list.isEmpty() ? list : List.of("Sin datos disponibles");
    }

    /** Extrae el bloque JSON aunque venga envuelto en markdown o texto extra. */
    private String extractJsonBlock(String raw) {
        String text = raw.trim();
        if (text.startsWith("```")) {
            text = text.replaceAll("(?s)^```(?:json)?\\s*", "").replaceAll("```\\s*$", "").trim();
        }
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return text;
    }

    private List<String> readStringList(JsonNode node, String... fieldNames) {
        for (String field : fieldNames) {
            JsonNode value = node.path(field);
            List<String> parsed = parseStringListValue(value);
            if (!parsed.isEmpty()) {
                return parsed;
            }
        }
        return List.of();
    }

    private List<String> readStringListDeep(JsonNode node, String... fieldNames) {
        if (node == null || node.isMissingNode()) {
            return List.of();
        }
        List<String> direct = readStringList(node, fieldNames);
        if (!direct.isEmpty()) {
            return direct;
        }
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                List<String> nested = readStringListDeep(fields.next().getValue(), fieldNames);
                if (!nested.isEmpty()) {
                    return nested;
                }
            }
        }
        return List.of();
    }

    private List<String> parseStringListValue(JsonNode value) {
        if (value.isMissingNode() || value.isNull()) {
            return List.of();
        }
        if (value.isArray()) {
            return toStringList(value);
        }
        if (value.isTextual()) {
            return splitTextToList(value.asText());
        }
        return List.of();
    }

    private List<String> splitTextToList(String text) {
        List<String> list = new ArrayList<>();
        for (String line : text.split("\\r?\\n")) {
            String trimmed = line.replaceAll("^[-•*\\d.)\\s]+", "").trim();
            if (!trimmed.isEmpty()) {
                list.add(trimmed);
            }
        }
        return list;
    }

    private List<String> toStringList(JsonNode node) {
        List<String> list = new ArrayList<>();
        if (!node.isArray()) {
            return list;
        }
        node.forEach(item -> {
            String text = extractItemText(item);
            if (text != null && !text.isBlank()) {
                list.add(text);
            }
        });
        return list;
    }

    private String extractItemText(JsonNode item) {
        if (item.isTextual() || item.isNumber()) {
            return item.asText().trim();
        }
        if (item.isObject()) {
            for (String key : List.of("text", "descripcion", "description", "value", "item")) {
                if (item.has(key) && item.get(key).isTextual()) {
                    return item.get(key).asText().trim();
                }
            }
        }
        return null;
    }
}
