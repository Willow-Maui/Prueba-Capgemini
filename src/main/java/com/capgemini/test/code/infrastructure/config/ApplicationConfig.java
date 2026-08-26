package com.capgemini.test.code.infrastructure.config;

import com.capgemini.test.code.application.usecase.user.CreateUserUseCase;
import com.capgemini.test.code.application.usecase.user.GetUserUseCase;
import com.capgemini.test.code.domain.user.repository.UserRepository;
import com.capgemini.test.code.domain.room.repository.RoomRepository;
import com.capgemini.test.code.application.ports.output.DniValidationPort;
import com.capgemini.test.code.application.ports.output.NotificationPort;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.user.UserPersistenceAdapter;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.writedb.room.RoomPersistenceAdapter;
import com.capgemini.test.code.infrastructure.adapter.output.external.dni.DniValidationAdapter;
import com.capgemini.test.code.infrastructure.adapter.output.external.notification.NotificationAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ApplicationConfig - Configuración de inyección de dependencias
 *
 * Define los beans de:
 * - Use Cases
 * - Adaptadores (persistence, external)
 * - Puertos
 * - Utilities
 *
 * Patrón: Dependency Injection (Spring)
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserPersistenceAdapter userPersistenceAdapter;
    private final RoomPersistenceAdapter roomPersistenceAdapter;
    private final DniValidationAdapter dniValidationAdapter;
    private final NotificationAdapter notificationAdapter;

    // ==================== PORTS ====================

    @Bean
    public UserRepository userRepository() {
        return userPersistenceAdapter;
    }

    @Bean
    public RoomRepository roomRepository() {
        return roomPersistenceAdapter;
    }

    @Bean
    public DniValidationPort dniValidationPort() {
        return dniValidationAdapter;
    }

    @Bean
    public NotificationPort notificationPort() {
        return notificationAdapter;
    }

    // ==================== USE CASES ====================

    @Bean
    public CreateUserUseCase createUserUseCase() {
        return new CreateUserUseCase(
            userRepository(),
            dniValidationPort(),
            notificationPort()
        );
    }

    @Bean
    public GetUserUseCase getUserUseCase() {
        return new GetUserUseCase(userRepository());
    }

    // ==================== UTILITIES ====================

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}

