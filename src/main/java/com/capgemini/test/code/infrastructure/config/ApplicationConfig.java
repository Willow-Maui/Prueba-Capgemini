package com.capgemini.test.code.infrastructure.config;

import com.capgemini.test.code.application.usecase.user.CreateUserUseCase;
import com.capgemini.test.code.application.usecase.user.GetUserUseCase;
import com.capgemini.test.code.domain.user.repository.UserRepository;
import com.capgemini.test.code.domain.room.repository.RoomRepository;
import com.capgemini.test.code.application.ports.output.DniValidationPort;
import com.capgemini.test.code.application.ports.output.NotificationPort;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.commondb.user.UserPersistenceAdapter;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.readdb.user.UserReadJpaRepository;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.user.UserWriteAdapter;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.room.RoomPersistenceAdapter;
import com.capgemini.test.code.infrastructure.adapter.output.external.dni.DniValidationAdapter;
import com.capgemini.test.code.infrastructure.adapter.output.external.notification.NotificationAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de inyección de dependencias (ApplicationConfig).
 *
 * Responsabilidad:
 * - Instanciar beans de Use Cases sin @Component ni @Service
 * - Inyectar puertos (interfaces) en Use Cases
 * - Exponer adaptadores como beans de puertos
 * - Configurar utilidades (ObjectMapper, etc.)
 *
 * Patrón: Inversión de Control (IoC) mediante @Bean
 * Los Use Cases se instancian manualmente, no con @Component,
 * garantizando que Application Layer es agnóstico de Spring.
 *
 * Capas representadas:
 * - Ports (UserRepository, DniValidationPort, NotificationPort) → Interfaces
 * - Adapters (UserPersistenceAdapter, DniValidationAdapter, etc.) → Implementaciones
 * - Use Cases (CreateUserUseCase, GetUserUseCase) → Orquestación
 */
@Configuration
public class ApplicationConfig {

    // ==================== PORTS ====================
    // Exponen adaptadores como puertos

    /**
     * Expone el adaptador de persistencia como puerto de repositorio de usuario.
     *
     * @return UserRepository interface, implementada por UserPersistenceAdapter
     */
    @Bean
    public UserRepository userRepository(UserPersistenceAdapter userPersistenceAdapter) {
        return userPersistenceAdapter;
    }

    /**
     * Expone el adaptador de persistencia como puerto de repositorio de sala.
     *
     * @return RoomRepository interface, implementada por RoomPersistenceAdapter
     */
    @Bean
    public RoomRepository roomRepository(RoomPersistenceAdapter roomPersistenceAdapter) {
        return roomPersistenceAdapter;
    }

    /**
     * Expone el adaptador externo como puerto de validación de DNI.
     *
     * @return DniValidationPort interface, implementada por DniValidationAdapter
     */
    @Bean
    public DniValidationPort dniValidationPort(DniValidationAdapter dniValidationAdapter) {
        return dniValidationAdapter;
    }

    /**
     * Expone el adaptador externo como puerto de notificaciones.
     *
     * @return NotificationPort interface, implementada por NotificationAdapter
     */
    @Bean
    public NotificationPort notificationPort(NotificationAdapter notificationAdapter) {
        return notificationAdapter;
    }

    // ==================== USE CASES ====================
    // Instancia Use Cases manualmente (sin @Component)
    // Esto garantiza que Application Layer es agnóstico de Spring

    /**
     * Instancia el Use Case de creación de usuario.
     *
     * Inyecta:
     * - UserRepository (para persistencia)
     * - DniValidationPort (para validación externa)
     * - NotificationPort (para notificaciones)
     *
     * @return CreateUserUseCase bean
     */
    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository,
                                               DniValidationPort dniValidationPort,
                                               NotificationPort notificationPort) {
        return new CreateUserUseCase(
            userRepository,
            dniValidationPort,
            notificationPort
        );
    }

    /**
     * Instancia el Use Case de lectura de usuario.
     *
     * Inyecta:
     * - UserRepository (para persistencia)
     *
     * @return GetUserUseCase bean
     */
    @Bean
    public GetUserUseCase getUserUseCase(UserPersistenceAdapter userRepository) {
        return new GetUserUseCase(userRepository);
    }

    // ==================== UTILITIES ====================

    /**
     * Configura ObjectMapper para serialización/deserialización JSON.
     *
     * Características:
     * - Soporta LocalDateTime y LocalDate
     * - No serializa fechas como timestamps
     *
     * @return ObjectMapper configurado
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}

