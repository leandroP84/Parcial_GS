# Informe Técnico — SmartCV IA

> Bitácora del desarrollo asistido por IA para el TP Integrador.

---

## 1. Herramientas de IA Utilizadas

| Herramienta | Uso en el proyecto |
|-------------|-------------------|
| **Cursor** | IDE principal con agente de IA para generar arquitectura, código backend/frontend, Docker, CI y documentación |
| **Google Gemini API** | Motor de IA integrado en la aplicación para análisis de CV y generación de cartas de presentación |
| **Claude/GPT (vía Cursor)** | Modelos de lenguaje que potenciaron la generación y revisión de código durante el desarrollo |

---

## 2. Cómo se Utilizó Cursor

Cursor actuó como **arquitecto y desarrollador asistido** en todo el ciclo del proyecto:

### Fase de diseño
- Se solicitó la arquitectura completa, modelo ER y estructura de carpetas antes de escribir código.
- Cursor generó el documento `docs/ARQUITECTURA.md` con diagramas, endpoints y decisiones técnicas.

### Fase de implementación
- Generación del backend Spring Boot completo: entidades JPA, repositorios, servicios, DTOs, mappers, seguridad JWT y controladores REST.
- Generación del frontend React con Material UI: páginas, componentes, rutas protegidas y servicios Axios.
- Configuración de Docker, docker-compose, GitHub Actions y README.

### Ventajas observadas con Cursor
- **Velocidad:** Un proyecto full-stack que normalmente tomaría semanas se estructuró en horas.
- **Consistencia:** Patrones uniformes en controllers, services y componentes React.
- **Documentación:** README, arquitectura e informe generados en paralelo al código.
- **Corrección iterativa:** El agente detecta errores de compilación y los corrige antes de continuar.

---

## 3. Cómo se Utilizó Gemini

Gemini se integró como **servicio de IA en producción** dentro de la aplicación:

### Servicio `GeminiService`
- Construye prompts estructurados con todos los datos del CV del usuario.
- Llama a la API REST de Google Gemini (`gemini-2.5-flash`, con fallback automático entre modelos).
- Parsea respuestas JSON para análisis o texto plano para cartas.

### Endpoints
- `POST /api/ai/analyze-cv` — Devuelve fortalezas, debilidades, recomendaciones y habilidades sugeridas.
- `POST /api/ai/generate-cover-letter` — Genera carta de presentación profesional.

### Prompt de análisis (ejemplo)
```
Eres un experto en recursos humanos y empleabilidad.
Analiza el siguiente perfil profesional y responde ÚNICAMENTE con un JSON válido...
```

### Prompt de carta (ejemplo)
```
Eres un redactor profesional de cartas de presentación.
Genera una carta de presentación en español, profesional y persuasiva...
```

---

## 4. Ejemplos de Prompts Utilizados

### Prompt 1 — Arquitectura (funcionó muy bien)
```
Actúa como arquitecto de software senior...
Primero diseña la arquitectura completa del sistema, el modelo entidad-relación
y la estructura de carpetas antes de comenzar a programar.
```
**Resultado:** Documento de arquitectura completo con diagramas Mermaid, API REST y plan de fases.

### Prompt 2 — Implementación completa (funcionó bien)
```
Ahora continúa con la implementación completa del proyecto SmartCV IA.
Quiero una versión funcional lista para una entrega universitaria.
[Lista detallada de módulos y requisitos]
```
**Resultado:** Backend, frontend, Docker, CI y documentación generados de forma coherente.

### Prompt 3 — Corrección de errores (funcionó bien)
```
Cuando termines cada módulo, verifica dependencias faltantes y corrige errores
de compilación antes de continuar.
```
### Prompt 4 — Auditoría y corrección en producción local (funcionó bien)
```
Realiza una auditoría completa del proyecto... Corrige automáticamente todo lo que encuentres.
```
**Resultado:** Se corrigieron JWT, CORS, Docker, modelos de Gemini y el parser de respuestas JSON.

### Prompt 5 — Depuración asistida (funcionó bien)
Describir el error exacto de la terminal o captura de pantalla (puertos Docker, 404/429 de Gemini, parseo JSON) y pedir la causa + solución.
**Resultado:** El agente identificó conflictos de puertos con pgAdmin, cuota `limit: 0` en modelos deprecados y mejoró el `GeminiService` iterativamente.

---

## 5. Ventajas Obtenidas

| Área | Ventaja |
|------|---------|
| Productividad | Reducción estimada del 70-80% en tiempo de scaffolding |
| Calidad estructural | Capas bien separadas desde el inicio (DTOs, mappers, services) |
| Documentación | README, arquitectura e informe generados automáticamente |
| DevOps | Docker Compose y GitHub Actions configurados sin esfuerzo manual |
| Aprendizaje | El código generado sirve como referencia de buenas prácticas Spring/React |

---

## 6. Limitaciones y Problemas Encontrados

Durante el desarrollo y las pruebas locales, el grupo enfrentó varios obstáculos técnicos. A continuación se documentan los más relevantes y cómo los resolvimos.

### 6.1 Infraestructura y Docker

| Problema | Qué nos pasó | Solución aplicada |
|----------|--------------|-------------------|
| Conflicto de puertos PostgreSQL | Al ejecutar `docker-compose up`, Docker no podía usar el puerto 5432 porque ya lo tenía ocupado nuestra instalación local de PostgreSQL (pgAdmin4). El error era: *"ports are not available... bind: Intento de acceso a un socket no permitido"*. | Removimos el mapeo de puertos de PostgreSQL en `docker-compose.yml`. La base de datos queda accesible solo dentro de la red interna de Docker; el backend se conecta por `postgres:5432` sin exponer el puerto al host. |
| Conflicto en puerto 5433 | Incluso al cambiar a 5433, Windows seguía bloqueando el bind por permisos o rangos reservados. | Confirmamos que no era necesario exponer PostgreSQL al exterior para probar la aplicación; solo hace falta el frontend en `:3000` y el backend en `:8080`. |
| Terminal "colgada" en Docker | Pensamos que el sistema no había terminado de iniciar porque la terminal quedaba mostrando logs sin un mensaje de "listo". | Aprendimos que `docker-compose up` **no termina**: se queda en vivo mostrando logs. El indicador de éxito es ver `Started SmartCvApplication` y `Container smartcv-backend Healthy`. |

### 6.2 Integración con Google Gemini API

| Problema | Qué nos pasó | Solución aplicada |
|----------|--------------|-------------------|
| Modelo no encontrado (404) | El análisis de CV fallaba con error 404: `models/gemini-1.5-flash is not found for API version v1beta`. El modelo configurado inicialmente ya no estaba disponible en la API. | Actualizamos el modelo por defecto a `gemini-2.5-flash` y agregamos fallback automático entre varios modelos si uno no existe. |
| Cuota agotada con `limit: 0` (429) | Tras corregir el 404, apareció error 429 con el mensaje `limit: 0, model: gemini-2.0-flash`. Esperamos varios minutos y el error persistía. | Entendimos que **no era un rate limit temporal**: Google había dejado la cuota gratuita de `gemini-2.0-flash` en cero. Cambiamos a `gemini-2.5-flash` y el backend prueba otros modelos si detecta `limit: 0`. |
| Configuración de API Key | No sabíamos dónde pegar la clave ni cómo reiniciar el backend para que la tomara. | Creamos el archivo `.env` en la raíz con `GEMINI_API_KEY` y reconstruimos el contenedor con `docker-compose up --build backend`. |
| Error al parsear respuesta de IA | Gemini respondía correctamente, pero la app mostraba: *"No se pudo parsear / interpretar el análisis de IA"*. La respuesta no venía en JSON limpio o usaba formatos inesperados. | Implementamos `responseMimeType: application/json`, un esquema JSON obligatorio (`responseSchema`), y un parser más tolerante (markdown, JSON anidado, claves en inglés/español). |

### 6.3 Otras limitaciones del desarrollo asistido por IA

| Limitación | Descripción | Mitigación |
|------------|-------------|------------|
| Respuestas JSON de Gemini | A veces incluye markdown (```json) alrededor del JSON | Parser que limpia bloques de código antes de parsear |
| Lazy loading JPA | Acceso a relaciones fuera de transacción causa errores | `@Transactional(readOnly=true)` y consultas explícitas por repositorio |
| Modelo de entidades | La especificación del TP difiere de la arquitectura inicial | Priorizamos la especificación más reciente del enunciado |
| Tests | Cobertura de tests básica (context load) | Suficiente para CI; tests de integración son mejora futura |
| API Key ausente | Sin key configurada, los endpoints de IA fallan | Validación explícita en `GeminiService` con mensaje claro |

---

## 7. Lecciones Aprendidas del Grupo

1. **Diseñar antes de codificar** acelera el desarrollo asistido por IA. Un buen prompt de arquitectura evita refactorizaciones costosas.

2. **Prompts específicos y estructurados** producen mejores resultados que instrucciones vagas. Listar módulos, endpoints y entidades mejora la coherencia del código generado.

3. **La IA es excelente para scaffolding** pero requiere supervisión humana en seguridad (JWT secrets, CORS), despliegue y pruebas end-to-end.

4. **Integrar IA en la app (Gemini) y usar IA para desarrollar (Cursor)** son dos casos de uso complementarios que demuestran el dominio del tema del TP.

5. **Verificar compilación en cada módulo** es crítico cuando se genera mucho código de una vez.

6. **Docker en Windows no es plug-and-play** si ya tenemos servicios locales (PostgreSQL, pgAdmin). Hay que revisar conflictos de puertos y entender que los contenedores se comunican por red interna sin necesidad de exponer todos los puertos.

7. **Los modelos de Gemini cambian y deprecan cuotas**. No alcanza con copiar un tutorial: hay que leer el mensaje de error (`404` vs `429 limit: 0`), probar modelos alternativos (`gemini-2.5-flash`) y consultar la documentación actualizada de Google AI Studio.

8. **Un error 429 no siempre significa "esperar un poco"**. Cuando el mensaje incluye `limit: 0`, el problema es de configuración de cuenta o modelo, no de haber hecho demasiados intentos seguidos.

9. **La integración con LLMs requiere parseo defensivo**. Aunque pidamos JSON en el prompt, conviene forzar el formato con `responseSchema` y tener un parser que tolere variaciones (markdown, campos anidados, idiomas distintos).

10. **Probar el flujo completo en local antes del deploy** nos ahorró llevar errores a producción. El grupo validó login, CRUD, dashboard, análisis de CV y carta de presentación con Docker antes de pensar en Render/Vercel.

11. **Documentar los errores en equipo** (este informe) ayuda a la defensa oral: demuestra que entendimos los problemas reales, no solo que copiamos código generado por IA.

---

## 8. Conclusión

El desarrollo de SmartCV IA demostró que las herramientas modernas de IA (Cursor + Gemini) permiten crear aplicaciones full-stack de calidad universitaria en tiempo récord, manteniendo buenas prácticas de arquitectura, seguridad y despliegue. El valor agregado está en la **integración coherente de IA en el flujo del producto**, no solo en la generación de código.

---

*Informe generado como parte de la entrega del TP Integrador — Desarrollo Ágil Asistido por IA.*
