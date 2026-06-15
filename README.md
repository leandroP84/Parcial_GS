# SmartCV IA

Plataforma web para crear, administrar y mejorar currículums utilizando inteligencia artificial. Permite cargar datos profesionales y obtener recomendaciones inteligentes para mejorar la empleabilidad.

## Demo

| Servicio | URL |
|----------|-----|
| Frontend (Vercel) | `https://tu-app.vercel.app` |
| Backend (Render) | `https://tu-backend.onrender.com` |

## Tecnologías

### Frontend
- React 18 + Vite
- Material UI
- Axios
- React Router

### Backend
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- Lombok
- Maven

### Base de Datos
- PostgreSQL

### IA
- Google Gemini API (`gemini-1.5-flash`)

### DevOps
- Docker + Docker Compose
- GitHub Actions (CI)
- Vercel (frontend) + Render (backend) + Neon (DB)

## Funcionalidades

- Registro y login con JWT
- Perfil profesional (resumen, teléfono, LinkedIn, GitHub)
- CRUD de experiencia laboral, educación y habilidades
- Dashboard con estadísticas
- Análisis de CV con IA (fortalezas, debilidades, recomendaciones)
- Generación de carta de presentación con IA

## Instalación rápida con Docker

### Requisitos
- Docker y Docker Compose
- API Key de Google Gemini ([obtener aquí](https://aistudio.google.com/apikey))

### Pasos

1. Clonar el repositorio:
```bash
git clone https://github.com/tu-usuario/smartcv.git
cd smartcv
```

2. Configurar variables de entorno:
```bash
cp .env.example .env
# Editar .env y agregar GEMINI_API_KEY
```

3. Levantar todos los servicios:
```bash
docker-compose up --build
```

4. Acceder a la aplicación:
- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080/api
- **PostgreSQL:** localhost:5432

### Usuarios demo (creados automáticamente)

| Rol | Email | Contraseña |
|-----|-------|------------|
| Usuario | `demo@smartcv.com` | `demo123` |
| Admin | `admin@smartcv.com` | `admin123` |

El usuario demo incluye perfil, 2 experiencias, 2 estudios y 5 habilidades precargadas.

## Instalación local (sin Docker)

### Backend

```bash
cd backend
# Configurar PostgreSQL local y variables de entorno
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend en http://localhost:5173

## Variables de entorno

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `GEMINI_API_KEY` | API Key de Google Gemini | `AIza...` |
| `JWT_SECRET` | Clave secreta JWT (Base64, min 256 bits) | Ver `.env.example` |
| `JWT_EXPIRATION` | Expiración del token en ms | `86400000` |
| `DATABASE_URL` | URL JDBC de PostgreSQL | `jdbc:postgresql://...` |
| `DB_USERNAME` | Usuario de BD | `smartcv` |
| `DB_PASSWORD` | Contraseña de BD | `smartcv123` |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos (separados por coma) | `http://localhost:5173` |
| `VITE_API_BASE_URL` | URL base del API (frontend) | `http://localhost:8080/api` |

## API Endpoints

### Autenticación (público)
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/register` | Registro |
| POST | `/api/auth/login` | Login |

### Perfil
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/perfil` | Obtener perfil |
| PUT | `/api/perfil` | Guardar perfil |

### CRUDs
| Recurso | Base URL |
|---------|----------|
| Experiencias | `/api/experiencias` |
| Educación | `/api/educaciones` |
| Habilidades | `/api/habilidades` |

### Dashboard e IA
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/dashboard/stats` | Estadísticas |
| POST | `/api/ai/analyze-cv` | Analizar CV |
| POST | `/api/ai/generate-cover-letter` | Generar carta |

## Despliegue

### PostgreSQL en Neon
1. Crear proyecto en [Neon](https://neon.tech)
2. Copiar connection string y agregar prefijo `jdbc:`:
   ```
   jdbc:postgresql://ep-xxx.region.aws.neon.tech/neondb?sslmode=require
   ```

### Backend en Render
1. Crear **Web Service** conectado al repo
2. Root Directory: `backend`
3. Build Command: `mvn clean package -DskipTests`
4. Start Command: `java -jar target/smartcv-backend-1.0.0.jar`
5. Variables de entorno: `DATABASE_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `GEMINI_API_KEY`, `CORS_ALLOWED_ORIGINS`

### Frontend en Vercel
1. Importar repo en [Vercel](https://vercel.com)
2. Root Directory: `frontend`
3. Framework Preset: Vite
4. Variable: `VITE_API_BASE_URL=https://tu-backend.onrender.com/api`

## Capturas sugeridas

Para el README y la entrega, se recomienda capturar:

1. Pantalla de login/registro
2. Dashboard con estadísticas
3. Formulario de perfil profesional
4. CRUD de experiencias laborales
5. Resultado del análisis de CV con IA
6. Carta de presentación generada
7. Pipeline de GitHub Actions en verde

## Uso de IA

SmartCV IA integra **Google Gemini** para:

- **Analizar CV:** Evalúa el perfil completo y devuelve fortalezas, debilidades, recomendaciones y habilidades sugeridas.
- **Generar carta de presentación:** Crea una carta profesional personalizada basada en el perfil del usuario.

La IA fue asistida en el desarrollo con **Cursor** (ver `docs/INFORME-TECNICO.md`).

## Estructura del proyecto

```
smartcv/
├── backend/          # Spring Boot API
├── frontend/         # React + Vite
├── docs/             # Arquitectura e informe técnico
├── docker-compose.yml
└── .github/workflows/ci.yml
```

## Postman

Importar la colección en `docs/postman/SmartCV-IA.postman_collection.json` para probar todos los endpoints.

## Checklist de entrega

Ver `CHECKLIST_PRE_ENTREGA.md` para pasos detallados de demo, despliegue y grabación.

## Autor

Proyecto universitario — TP Integrador: Desarrollo Ágil Asistido por IA.

## Licencia

MIT
