package com.capgemini.test.code.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests para los DTOs de aplicación.
 * Verifica que los DTOs se construyen, mapean y getters/setters funcionan correctamente.
 */
@DisplayName("Application DTOs - Capas de Transferencia")
class ApplicationDtoTests {

    @Nested
    @DisplayName("UserDTO")
    class UserDtoTests {

        @Test
        @DisplayName("Construye UserDTO con builder completo")
        void buildCompleteUserDto() {
            UserDTO userDto = UserDTO.builder()
                    .id(1L)
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone("677998899")
                    .role("ADMIN")
                    .roomId(1L)
                    .build();

            assertThat(userDto.getId()).isEqualTo(1L);
            assertThat(userDto.getName()).isEqualTo("pablo");
            assertThat(userDto.getEmail()).isEqualTo("pablo@example.com");
            assertThat(userDto.getDni()).isEqualTo("23454234W");
            assertThat(userDto.getPhone()).isEqualTo("677998899");
            assertThat(userDto.getRole()).isEqualTo("ADMIN");
            assertThat(userDto.getRoomId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Construye UserDTO sin argumentos")
        void buildEmptyUserDto() {
            UserDTO userDto = new UserDTO();
            assertThat(userDto).isNotNull();
        }

        @Test
        @DisplayName("Constructor con todos los argumentos")
        void constructorAllArgs() {
            UserDTO userDto = new UserDTO(
                    42L, "juan", "juan@example.com", "677998899",
                    "12345678A", "SUPERADMIN", 2L
            );

            assertThat(userDto.getId()).isEqualTo(42L);
            assertThat(userDto.getName()).isEqualTo("juan");
            assertThat(userDto.getEmail()).isEqualTo("juan@example.com");
            assertThat(userDto.getPhone()).isEqualTo("677998899");
            assertThat(userDto.getDni()).isEqualTo("12345678A");
            assertThat(userDto.getRole()).isEqualTo("SUPERADMIN");
            assertThat(userDto.getRoomId()).isEqualTo(2L);
        }

        @Test
        @DisplayName("Setters funcionan correctamente")
        void settersWork() {
            UserDTO userDto = new UserDTO();
            userDto.setId(99L);
            userDto.setName("maria");
            userDto.setEmail("maria@example.com");
            userDto.setDni("87654321B");
            userDto.setPhone("666777888");
            userDto.setRole("ADMIN");
            userDto.setRoomId(3L);

            assertThat(userDto.getId()).isEqualTo(99L);
            assertThat(userDto.getName()).isEqualTo("maria");
            assertThat(userDto.getEmail()).isEqualTo("maria@example.com");
            assertThat(userDto.getDni()).isEqualTo("87654321B");
            assertThat(userDto.getPhone()).isEqualTo("666777888");
            assertThat(userDto.getRole()).isEqualTo("ADMIN");
            assertThat(userDto.getRoomId()).isEqualTo(3L);
        }

        @Test
        @DisplayName("Construye UserDTO con valores null")
        void buildWithNullValues() {
            UserDTO userDto = UserDTO.builder()
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone(null)
                    .role("ADMIN")
                    .build();

            assertThat(userDto.getId()).isNull();
            assertThat(userDto.getPhone()).isNull();
            assertThat(userDto.getRoomId()).isNull();
            assertThat(userDto.getName()).isEqualTo("pablo");
        }

        @Test
        @DisplayName("Builder solo con nombre y email")
        void builderMinimal() {
            UserDTO userDto = UserDTO.builder()
                    .name("pablo")
                    .email("pablo@example.com")
                    .build();

            assertThat(userDto.getName()).isEqualTo("pablo");
            assertThat(userDto.getEmail()).isEqualTo("pablo@example.com");
            assertThat(userDto.getId()).isNull();
            assertThat(userDto.getDni()).isNull();
            assertThat(userDto.getPhone()).isNull();
            assertThat(userDto.getRole()).isNull();
            assertThat(userDto.getRoomId()).isNull();
        }
    }

    @Nested
    @DisplayName("CreateUserRequest (Application DTO)")
    class CreateUserRequestTests {

        @Test
        @DisplayName("Construye CreateUserRequest con builder")
        void buildCompleteRequest() {
            CreateUserRequest request = CreateUserRequest.builder()
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone("677998899")
                    .rol("admin")
                    .build();

            assertThat(request.getName()).isEqualTo("pablo");
            assertThat(request.getEmail()).isEqualTo("pablo@example.com");
            assertThat(request.getDni()).isEqualTo("23454234W");
            assertThat(request.getPhone()).isEqualTo("677998899");
            assertThat(request.getRol()).isEqualTo("admin");
        }

        @Test
        @DisplayName("Constructor vacío")
        void buildEmpty() {
            CreateUserRequest request = new CreateUserRequest();
            assertThat(request).isNotNull();
        }

        @Test
        @DisplayName("Setters funcionan correctamente")
        void settersWork() {
            CreateUserRequest request = new CreateUserRequest();
            request.setName("juan");
            request.setEmail("juan@example.com");
            request.setDni("12345678A");
            request.setPhone("666777888");
            request.setRol("superadmin");

            assertThat(request.getName()).isEqualTo("juan");
            assertThat(request.getEmail()).isEqualTo("juan@example.com");
            assertThat(request.getDni()).isEqualTo("12345678A");
            assertThat(request.getPhone()).isEqualTo("666777888");
            assertThat(request.getRol()).isEqualTo("superadmin");
        }
    }

    @Nested
    @DisplayName("CreateUserResponse (Application DTO)")
    class CreateUserResponseTests {

        @Test
        @DisplayName("Construye CreateUserResponse con builder")
        void buildCompleteResponse() {
            CreateUserResponse response = CreateUserResponse.builder()
                    .id(1L)
                    .name("pablo")
                    .email("pablo@example.com")
                    .rol("admin")
                    .roomId(1L)
                    .build();

            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getName()).isEqualTo("pablo");
            assertThat(response.getEmail()).isEqualTo("pablo@example.com");
            assertThat(response.getRol()).isEqualTo("admin");
            assertThat(response.getRoomId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Constructor vacío")
        void buildEmpty() {
            CreateUserResponse response = new CreateUserResponse();
            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("Setters funcionan correctamente")
        void settersWork() {
            CreateUserResponse response = new CreateUserResponse();
            response.setId(42L);
            response.setName("carlos");
            response.setEmail("carlos@example.com");
            response.setRol("superadmin");
            response.setRoomId(2L);

            assertThat(response.getId()).isEqualTo(42L);
            assertThat(response.getName()).isEqualTo("carlos");
            assertThat(response.getEmail()).isEqualTo("carlos@example.com");
            assertThat(response.getRol()).isEqualTo("superadmin");
            assertThat(response.getRoomId()).isEqualTo(2L);
        }
    }
}

