-- SmartCV IA — Script de referencia del modelo relacional
-- PostgreSQL 15+

-- Enums (implementados como VARCHAR con CHECK o como tipos ENUM nativos)

CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');
CREATE TYPE skill_level AS ENUM ('BASICO', 'INTERMEDIO', 'AVANZADO', 'EXPERTO');

-- ============================================================
-- USERS
-- ============================================================
CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        user_role NOT NULL DEFAULT 'USER',
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ============================================================
-- PROFILES (1:1 con users)
-- ============================================================
CREATE TABLE profiles (
    id                   BIGSERIAL PRIMARY KEY,
    user_id              BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    first_name           VARCHAR(100),
    last_name            VARCHAR(100),
    phone                VARCHAR(30),
    linkedin_url         VARCHAR(500),
    github_url           VARCHAR(500),
    professional_summary TEXT,
    created_at           TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ============================================================
-- WORK EXPERIENCES (N:1 con users)
-- ============================================================
CREATE TABLE work_experiences (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    company     VARCHAR(200) NOT NULL,
    position    VARCHAR(200) NOT NULL,
    start_date  DATE NOT NULL,
    end_date    DATE,  -- NULL = trabajo actual
    description TEXT,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_work_dates CHECK (end_date IS NULL OR end_date >= start_date)
);

-- ============================================================
-- EDUCATIONS (N:1 con users)
-- ============================================================
CREATE TABLE educations (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    institution  VARCHAR(200) NOT NULL,
    degree       VARCHAR(200) NOT NULL,
    start_year   INT NOT NULL,
    end_year     INT,  -- NULL = en curso
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_edu_years CHECK (
        start_year >= 1950 AND start_year <= 2100
        AND (end_year IS NULL OR (end_year >= start_year AND end_year <= 2100))
    )
);

-- ============================================================
-- SKILLS (N:1 con users)
-- ============================================================
CREATE TABLE skills (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name        VARCHAR(100) NOT NULL,
    level       skill_level NOT NULL DEFAULT 'INTERMEDIO',
    created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_skill_per_user UNIQUE (user_id, name)
);

-- ============================================================
-- ÍNDICES
-- ============================================================
CREATE INDEX idx_profiles_user_id ON profiles(user_id);
CREATE INDEX idx_work_experiences_user_id ON work_experiences(user_id);
CREATE INDEX idx_educations_user_id ON educations(user_id);
CREATE INDEX idx_skills_user_id ON skills(user_id);

-- ============================================================
-- Datos de ejemplo (desarrollo)
-- ============================================================
-- INSERT INTO users (email, password, role) VALUES
-- ('admin@smartcv.com', '$2a$10$...', 'ADMIN'),
-- ('user@smartcv.com', '$2a$10$...', 'USER');
