# Checklist Pre-Entrega — SmartCV IA

> Usa esta lista antes de presentar el TP Integrador. Marca cada ítem al completarlo.

---

## 1. Requisitos previos

- [ ] Docker Desktop instalado y en ejecución
- [ ] Node.js 20+ instalado (desarrollo local sin Docker)
- [ ] Java 17+ y Maven instalados (desarrollo local sin Docker)
- [ ] Cuenta en [Google AI Studio](https://aistudio.google.com/apikey) con API Key de Gemini
- [ ] Cuentas gratuitas en GitHub, Render, Vercel y Neon

---

## 2. Ejecutar localmente con Docker (recomendado para la demo)

### 2.1 Configuración inicial

```bash
git clone <tu-repo>
cd smartcv
cp .env.example .env
```

- [ ] Editar `.env` y agregar `GEMINI_API_KEY=tu_clave_real`

### 2.2 Levantar el sistema

```bash
docker-compose up --build
```

Esperar hasta ver en logs del backend:
```
Datos demo cargados:
  ADMIN → admin@smartcv.com / admin123
  USER  → demo@smartcv.com / demo123
```

- [ ] PostgreSQL healthy
- [ ] Backend healthy (`http://localhost:8080/actuator/health` → `{"status":"UP"}`)
- [ ] Frontend accesible en **http://localhost:3000**

### 2.3 Credenciales demo

| Rol | Email | Contraseña |
|-----|-------|------------|
| Usuario | `demo@smartcv.com` | `demo123` |
| Admin | `admin@smartcv.com` | `admin123` |

- [ ] Login con `demo@smartcv.com` funciona
- [ ] Dashboard muestra 2 experiencias, 2 estudios, 5 habilidades

### 2.4 Desarrollo local sin Docker (alternativa)

**Terminal 1 — PostgreSQL:** usar Docker solo para DB o instancia local.

**Terminal 2 — Backend:**
```bash
cd backend
# Variables: DATABASE_URL, JWT_SECRET, GEMINI_API_KEY, CORS_ALLOWED_ORIGINS
mvn spring-boot:run
```

**Terminal 3 — Frontend:**
```bash
cd frontend
npm install
npm run dev
```

- [ ] Frontend en http://localhost:5173
- [ ] Backend en http://localhost:8080/api

---

## 3. Probar funcionalidades (checklist de QA)

### Autenticación
- [ ] Registro de nuevo usuario
- [ ] Login con credenciales correctas
- [ ] Acceso denegado sin token (probar URL protegida en Postman)
- [ ] Logout desde el navbar

### Perfil y CRUD
- [ ] Ver y editar perfil profesional
- [ ] Crear, editar y eliminar experiencia laboral
- [ ] Crear, editar y eliminar educación
- [ ] Crear, editar y eliminar habilidad
- [ ] Dashboard actualiza contadores tras cambios

### Inteligencia Artificial
- [ ] **Analizar CV** devuelve fortalezas, debilidades, recomendaciones y habilidades sugeridas
- [ ] **Generar carta** produce texto profesional
- [ ] Sin `GEMINI_API_KEY` muestra error claro (no crash)

### API con Postman
- [ ] Importar `docs/postman/SmartCV-IA.postman_collection.json`
- [ ] Ejecutar **Login Demo** → token guardado automáticamente
- [ ] Probar GET Perfil, GET Stats, POST Analizar CV

### Admin (opcional)
- [ ] Login como `admin@smartcv.com`
- [ ] `GET /api/admin/users` devuelve lista (vía Postman)

---

## 4. Desplegar en Neon (PostgreSQL)

1. Crear proyecto en [neon.tech](https://neon.tech)
2. Copiar **connection string** de PostgreSQL
3. Agregar prefijo `jdbc:` a la URL:
   ```
   jdbc:postgresql://ep-xxx.region.aws.neon.tech/neondb?sslmode=require
   ```

- [ ] Base de datos creada en Neon
- [ ] Connection string con `?sslmode=require`
- [ ] Usuario y contraseña anotados para Render

> **Producción:** configurar `SEED_DATA=false` y `SPRING_PROFILES_ACTIVE=prod` en Render.

---

## 5. Desplegar backend en Render

1. Ir a [render.com](https://render.com) → New → Web Service
2. Conectar repositorio de GitHub
3. Configuración:

| Campo | Valor |
|-------|-------|
| Root Directory | `backend` |
| Runtime | Docker (o Java) |
| Build Command | `mvn clean package -DskipTests` |
| Start Command | `java -jar target/smartcv-backend-1.0.0.jar` |

4. Variables de entorno:

| Variable | Valor |
|----------|-------|
| `DATABASE_URL` | `jdbc:postgresql://...neon...?sslmode=require` |
| `DB_USERNAME` | usuario Neon |
| `DB_PASSWORD` | contraseña Neon |
| `JWT_SECRET` | clave Base64 segura (generar nueva, no usar la de demo) |
| `JWT_EXPIRATION` | `86400000` |
| `GEMINI_API_KEY` | tu API key |
| `GEMINI_MODEL` | `gemini-1.5-flash` |
| `CORS_ALLOWED_ORIGINS` | `https://tu-app.vercel.app` |
| `SEED_DATA` | `false` |
| `SPRING_PROFILES_ACTIVE` | `prod` |

- [ ] Deploy exitoso en Render
- [ ] Health check: `https://tu-backend.onrender.com/actuator/health` → UP
- [ ] Probar login vía Postman contra URL de Render

> **Nota:** El plan free de Render puede tardar ~50s en "despertar" el servicio.

---

## 6. Desplegar frontend en Vercel

1. Ir a [vercel.com](https://vercel.com) → Import Project
2. Seleccionar el repositorio
3. Configuración:

| Campo | Valor |
|-------|-------|
| Root Directory | `frontend` |
| Framework | Vite |
| Build Command | `npm run build` |
| Output Directory | `dist` |

4. Variable de entorno:

| Variable | Valor |
|----------|-------|
| `VITE_API_BASE_URL` | `https://tu-backend.onrender.com/api` |

- [ ] Deploy exitoso en Vercel
- [ ] Login funciona en producción
- [ ] CORS configurado (backend tiene URL de Vercel en `CORS_ALLOWED_ORIGINS`)

---

## 7. GitHub y CI/CD

- [ ] Repositorio **público** en GitHub
- [ ] Docente agregado como colaborador o link compartido
- [ ] Pipeline CI en verde (`.github/workflows/ci.yml`)
- [ ] README actualizado con links de demo reales

```bash
git add .
git commit -m "Preparar entrega SmartCV IA"
git push origin main
```

- [ ] Push realizado sin archivos `.env` ni secretos

---

## 8. Documentación de entrega

- [ ] `README.md` con descripción, tecnologías, instalación y links de demo
- [ ] `docs/INFORME-TECNICO.md` con herramientas IA, prompts y lecciones aprendidas
- [ ] `docs/ARQUITECTURA.md` disponible
- [ ] Capturas de pantalla agregadas al README (opcional pero recomendado):
  - Login / Dashboard
  - CRUD de experiencias
  - Resultado análisis IA
  - Carta de presentación generada
  - GitHub Actions en verde

---

## 9. Grabar demo del sistema (5–8 minutos)

### Guión sugerido

| Minuto | Contenido |
|--------|-----------|
| 0:00 | Presentación: qué es SmartCV IA y stack tecnológico |
| 0:30 | Mostrar repositorio GitHub y CI en verde |
| 1:00 | Abrir app desplegada (o localhost:3000) |
| 1:30 | Login con `demo@smartcv.com` / `demo123` |
| 2:00 | Dashboard: estadísticas del CV demo |
| 2:30 | Navegar perfil, experiencias, educación, habilidades |
| 3:30 | Crear o editar un registro en vivo |
| 4:30 | **Analizar CV con IA** — mostrar resultados |
| 5:30 | **Generar carta de presentación** — leer extracto |
| 6:30 | Mencionar herramientas IA usadas (Cursor + Gemini) |
| 7:00 | Mostrar link de producción y cerrar |

### Herramientas de grabación
- OBS Studio (gratis)
- Loom
- Grabación de pantalla de Windows (Win + G)

- [ ] Audio claro y sin ruido de fondo
- [ ] Resolución mínima 1280×720
- [ ] Mostrar URL de producción al final
- [ ] Video subido (YouTube no listado / Drive) y link en README

---

## 10. Verificación final antes de enviar

| Criterio del TP | Peso | Estado |
|-----------------|------|--------|
| Despliegue exitoso (app online) | 40% | [ ] |
| Estructura y claridad (código + README) | 30% | [ ] |
| Dominio de la IA (Gemini integrado) | 30% | [ ] |
| Bonus: CI/CD con GitHub Actions | Extra | [ ] |

### Comandos de verificación rápida

```bash
# Backend
cd backend && mvn clean verify

# Frontend
cd frontend && npm run build

# Docker completo
docker-compose up --build -d
curl http://localhost:8080/actuator/health
```

- [ ] Todos los comandos anteriores pasan sin error
- [ ] Link de demo enviado al docente
- [ ] Informe técnico entregado (PDF o Markdown)

---

## Contacto de soporte rápido

| Problema | Solución |
|----------|----------|
| CORS error en producción | Verificar `CORS_ALLOWED_ORIGINS` incluye URL exacta de Vercel |
| 502 en análisis IA | Verificar `GEMINI_API_KEY` en Render |
| Backend no conecta a Neon | URL debe empezar con `jdbc:postgresql://` y tener `sslmode=require` |
| Docker frontend no llama API | Usar http://localhost:3000 (nginx proxy `/api`) |
| Render lento al iniciar | Normal en plan free; esperar 50–60 segundos |

---

*Última actualización: auditoría pre-entrega SmartCV IA*
