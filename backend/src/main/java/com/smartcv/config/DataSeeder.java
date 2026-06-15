package com.smartcv.config;

import com.smartcv.entity.*;
import com.smartcv.entity.enums.Role;
import com.smartcv.entity.enums.SkillLevel;
import com.smartcv.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Carga datos de demostración al iniciar la aplicación.
 * Activar con app.seed-data=true (por defecto en desarrollo/Docker).
 */
@Slf4j
@Component
@Profile("!test")
@ConditionalOnProperty(name = "app.seed-data", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.existsByEmail("demo@smartcv.com")) {
            log.info("Datos demo ya existen, omitiendo seed.");
            return;
        }

        log.info("Cargando datos de demostración...");

        // Usuario ADMIN
        Usuario admin = Usuario.builder()
                .nombre("Admin")
                .apellido("SmartCV")
                .email("admin@smartcv.com")
                .password(passwordEncoder.encode("admin123"))
                .rol(Role.ADMIN)
                .build();
        admin.setPerfilProfesional(PerfilProfesional.builder()
                .usuario(admin)
                .resumenProfesional("Administrador de la plataforma SmartCV IA.")
                .telefono("+54 11 0000-0000")
                .linkedin("https://linkedin.com/in/admin-smartcv")
                .github("https://github.com/admin-smartcv")
                .build());

        // Usuario DEMO con CV completo
        Usuario demo = Usuario.builder()
                .nombre("Lucía")
                .apellido("García")
                .email("demo@smartcv.com")
                .password(passwordEncoder.encode("demo123"))
                .rol(Role.USER)
                .build();

        PerfilProfesional perfilDemo = PerfilProfesional.builder()
                .usuario(demo)
                .resumenProfesional(
                        "Desarrolladora Full Stack con 3 años de experiencia en React y Spring Boot. " +
                        "Apasionada por crear soluciones web escalables y por la integración de IA en productos digitales.")
                .telefono("+54 11 4567-8901")
                .linkedin("https://linkedin.com/in/lucia-garcia")
                .github("https://github.com/luciagarcia")
                .build();
        demo.setPerfilProfesional(perfilDemo);

        ExperienciaLaboral exp1 = ExperienciaLaboral.builder()
                .usuario(demo)
                .empresa("TechSolutions SA")
                .cargo("Desarrolladora Full Stack")
                .fechaInicio(LocalDate.of(2022, 3, 1))
                .fechaFin(null)
                .descripcion("Desarrollo de aplicaciones web con React, Spring Boot y PostgreSQL. " +
                        "Implementación de APIs REST y despliegue en la nube.")
                .build();

        ExperienciaLaboral exp2 = ExperienciaLaboral.builder()
                .usuario(demo)
                .empresa("StartupLab")
                .cargo("Desarrolladora Junior")
                .fechaInicio(LocalDate.of(2020, 6, 1))
                .fechaFin(LocalDate.of(2022, 2, 28))
                .descripcion("Mantenimiento de sistemas legacy y migración a arquitectura de microservicios.")
                .build();

        Educacion edu1 = Educacion.builder()
                .usuario(demo)
                .institucion("Universidad Nacional")
                .carrera("Ingeniería en Sistemas")
                .fechaInicio(LocalDate.of(2016, 3, 1))
                .fechaFin(LocalDate.of(2020, 12, 15))
                .build();

        Educacion edu2 = Educacion.builder()
                .usuario(demo)
                .institucion("Coursera")
                .carrera("Machine Learning Specialization")
                .fechaInicio(LocalDate.of(2023, 1, 1))
                .fechaFin(LocalDate.of(2023, 6, 30))
                .build();

        Habilidad h1 = Habilidad.builder().usuario(demo).nombre("React").nivel(SkillLevel.AVANZADO).build();
        Habilidad h2 = Habilidad.builder().usuario(demo).nombre("Spring Boot").nivel(SkillLevel.AVANZADO).build();
        Habilidad h3 = Habilidad.builder().usuario(demo).nombre("PostgreSQL").nivel(SkillLevel.INTERMEDIO).build();
        Habilidad h4 = Habilidad.builder().usuario(demo).nombre("Docker").nivel(SkillLevel.INTERMEDIO).build();
        Habilidad h5 = Habilidad.builder().usuario(demo).nombre("Git").nivel(SkillLevel.AVANZADO).build();

        demo.getExperiencias().add(exp1);
        demo.getExperiencias().add(exp2);
        demo.getEducaciones().add(edu1);
        demo.getEducaciones().add(edu2);
        demo.getHabilidades().add(h1);
        demo.getHabilidades().add(h2);
        demo.getHabilidades().add(h3);
        demo.getHabilidades().add(h4);
        demo.getHabilidades().add(h5);

        usuarioRepository.save(admin);
        usuarioRepository.save(demo);

        log.info("Datos demo cargados:");
        log.info("  ADMIN → admin@smartcv.com / admin123");
        log.info("  USER  → demo@smartcv.com / demo123");
    }
}
