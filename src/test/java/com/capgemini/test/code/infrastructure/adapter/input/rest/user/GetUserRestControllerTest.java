package com.capgemini.test.code.infrastructure.adapter.input.rest.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.application.ports.input.GetUserInputPort;
import com.capgemini.test.code.domain.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests de integración para GetUserRestController.
 * Verifica que el controller retorna usuarios correctamente y maneja errores HTTP.
 */
@WebMvcTest(GetUserRestController.class)
@DisplayName("GetUserRestController - Integración HTTP")
class GetUserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetUserInputPort getUserUseCase;

    @Nested
    @DisplayName("GET /users/{id}")
    class GetUserEndpoint {

        @Test
        @DisplayName("Retorna 200 OK con usuario encontrado")
        void getUserReturns200() throws Exception {
            // Arrange
            UserDTO userDto = UserDTO.builder()
                    .id(1L)
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone(null)
                    .rol("ADMIN")
                    .roomId(1L)
                    .build();

            when(getUserUseCase.execute(1L)).thenReturn(userDto);

            // Act & Assert
            mockMvc.perform(get("/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("pablo"))
                    .andExpect(jsonPath("$.email").value("pablo@example.com"))
                    .andExpect(jsonPath("$.rol").value("ADMIN"))
                    .andExpect(jsonPath("$.roomId").value(1L));
        }

        @Test
        @DisplayName("Retorna 404 Not Found cuando usuario no existe")
        void getUserReturns404OnNotFound() throws Exception {
            // Arrange
            when(getUserUseCase.execute(999L))
                    .thenThrow(new UserNotFoundException(999L));

            // Act & Assert
            mockMvc.perform(get("/users/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(404))
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("Mapea correctamente campos del usuario")
        void mapsUserFieldsCorrectly() throws Exception {
            // Arrange
            UserDTO userDto = UserDTO.builder()
                    .id(42L)
                    .name("juan")
                    .email("juan@example.com")
                    .dni("12345678A")
                    .phone("677998899")
                    .rol("SUPERADMIN")
                    .roomId(2L)
                    .build();

            when(getUserUseCase.execute(42L)).thenReturn(userDto);

            // Act & Assert
            mockMvc.perform(get("/users/42"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(42L))
                    .andExpect(jsonPath("$.name").value("juan"))
                    .andExpect(jsonPath("$.email").value("juan@example.com"))
                    .andExpect(jsonPath("$.phone").value("677998899"))
                    .andExpect(jsonPath("$.rol").value("SUPERADMIN"))
                    .andExpect(jsonPath("$.roomId").value(2L));
        }

        @Test
        @DisplayName("Obtiene usuario con teléfono null")
        void getUserWithNullPhone() throws Exception {
            // Arrange
            UserDTO userDto = UserDTO.builder()
                    .id(1L)
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone(null)
                    .rol("ADMIN")
                    .roomId(1L)
                    .build();

            when(getUserUseCase.execute(1L)).thenReturn(userDto);

            // Act & Assert
            mockMvc.perform(get("/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.phone").isEmpty());
        }
    }
}




