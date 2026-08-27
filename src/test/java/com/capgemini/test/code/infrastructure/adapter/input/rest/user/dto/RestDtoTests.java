package com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests para DTOs de REST (input layer).
 * Verifica que los DTOs se construyen, mapean y getters/setters funcionan correctamente.
 */
@DisplayName("REST DTOs - Serialización HTTP")
class RestDtoTests {

    @Nested
    @DisplayName("CreateUserRequest")
    class CreateUserRequestTests {

        @Test
        @DisplayName("Construye con builder completo")
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
        @DisplayName("Construye con valores null")
        void buildWithNullValues() {
            CreateUserRequest request = CreateUserRequest.builder()
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone(null)
                    .rol("admin")
                    .build();

            assertThat(request.getPhone()).isNull();
        }

        @Test
        @DisplayName("Construye vacío sin argumentos")
        void buildEmpty() {
            CreateUserRequest request = new CreateUserRequest();
            assertThat(request).isNotNull();
        }

        @Test
        @DisplayName("Constructor con todos los argumentos")
        void constructorAllArgs() {
            CreateUserRequest request = new CreateUserRequest(
                    "pablo", "pablo@example.com", "23454234W", "677998899", "admin"
            );

            assertThat(request.getName()).isEqualTo("pablo");
            assertThat(request.getEmail()).isEqualTo("pablo@example.com");
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
    @DisplayName("CreateUserResponse")
    class CreateUserResponseTests {

        @Test
        @DisplayName("Construye con builder completo")
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

        @Test
        @DisplayName("Constructor con todos los argumentos")
        void constructorAllArgs() {
            CreateUserResponse response = new CreateUserResponse(
                    1L, "pablo", "pablo@example.com", "admin", 1L
            );

            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getName()).isEqualTo("pablo");
        }
    }

    @Nested
    @DisplayName("GetUserResponse")
    class GetUserResponseTests {

        @Test
        @DisplayName("Construye con builder completo")
        void buildCompleteResponse() {
            GetUserResponse response = GetUserResponse.builder()
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
            GetUserResponse response = new GetUserResponse();
            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("Setters funcionan correctamente")
        void settersWork() {
            GetUserResponse response = new GetUserResponse();
            response.setId(99L);
            response.setName("maria");
            response.setEmail("maria@example.com");
            response.setRol("admin");
            response.setRoomId(3L);

            assertThat(response.getId()).isEqualTo(99L);
            assertThat(response.getName()).isEqualTo("maria");
            assertThat(response.getEmail()).isEqualTo("maria@example.com");
            assertThat(response.getRol()).isEqualTo("admin");
            assertThat(response.getRoomId()).isEqualTo(3L);
        }
    }
}

