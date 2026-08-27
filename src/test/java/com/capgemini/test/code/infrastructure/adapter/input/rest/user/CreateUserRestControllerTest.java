package com.capgemini.test.code.infrastructure.adapter.input.rest.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.application.ports.input.CreateUserInputPort;
import com.capgemini.test.code.domain.user.exceptions.InvalidDniException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests de integración para CreateUserRestController.
 * Verifica que el controller mapea correctamente requests y retorna respuestas HTTP.
 */
@WebMvcTest(CreateUserRestController.class)
@DisplayName("CreateUserRestController - Integración HTTP")
class CreateUserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateUserInputPort createUserUseCase;

    @Nested
    @DisplayName("POST /api/v1/users")
    class CreateUserEndpoint {

        @Test
        @DisplayName("Retorna 201 Created con usuario creado")
        void createUserReturns201() throws Exception {
            // Arrange
            var request = new com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto.CreateUserRequest(
                    "pablo", "pablo@example.com", "23454234W", null, "admin"
            );

            UserDTO responseDto = UserDTO.builder()
                    .id(42L)
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone(null)
                    .rol("ADMIN")
                    .roomId(1L)
                    .build();

            when(createUserUseCase.execute(any(UserDTO.class))).thenReturn(responseDto);

            // Act & Assert
            mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(42L))
                    .andExpect(jsonPath("$.name").value("pablo"))
                    .andExpect(jsonPath("$.email").value("pablo@example.com"))
                    .andExpect(jsonPath("$.rol").value("ADMIN"))
                    .andExpect(jsonPath("$.roomId").value(1L));
        }

        @Test
        @DisplayName("Retorna 409 Conflict cuando DNI es inválido")
        void createUserReturns409OnInvalidDni() throws Exception {
            // Arrange
            var request = new com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto.CreateUserRequest(
                    "pablo", "pablo@example.com", "99999999W", null, "admin"
            );

            when(createUserUseCase.execute(any(UserDTO.class)))
                    .thenThrow(new InvalidDniException("DNI no válido"));

            // Act & Assert
            mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value(409))
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("Mapea correctamente campos del request")
        void mapsRequestFieldsCorrectly() throws Exception {
            // Arrange
            var request = new com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto.CreateUserRequest(
                    "juan", "juan@example.com", "12345678A", "677998899", "superadmin"
            );

            UserDTO responseDto = UserDTO.builder()
                    .id(99L)
                    .name("juan")
                    .email("juan@example.com")
                    .dni("12345678A")
                    .phone("677998899")
                    .rol("SUPERADMIN")
                    .roomId(1L)
                    .build();

            when(createUserUseCase.execute(any(UserDTO.class))).thenReturn(responseDto);

            // Act & Assert
            mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(99L))
                    .andExpect(jsonPath("$.name").value("juan"))
                    .andExpect(jsonPath("$.email").value("juan@example.com"));
        }
    }
}


