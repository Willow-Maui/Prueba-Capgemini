package com.capgemini.test.code.infrastructure.adapter.input.rest.user.mapper;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto.CreateUserRequest;
import com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto.CreateUserResponse;
import com.capgemini.test.code.infrastructure.adapter.input.rest.user.dto.GetUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests para UserRestMapper.
 * Verifica que los mapeos entre DTOs REST y DTOs de aplicación funcionan correctamente.
 */
@DisplayName("UserRestMapper - Conversión de DTOs REST")
class UserRestMapperTest {

    @Nested
    @DisplayName("toApplicationDTO - REST Request → Application DTO")
    class ToApplicationDtoTests {

        @Test
        @DisplayName("Mapea CreateUserRequest completo a UserDTO")
        void mapsCompleteCreateUserRequest() {
            CreateUserRequest request = CreateUserRequest.builder()
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone("677998899")
                    .rol("admin")
                    .build();

            UserDTO result = UserRestMapper.toApplicationDTO(request);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("pablo");
            assertThat(result.getEmail()).isEqualTo("pablo@example.com");
            assertThat(result.getDni()).isEqualTo("23454234W");
            assertThat(result.getPhone()).isEqualTo("677998899");
            assertThat(result.getRol()).isEqualTo("admin");
            assertThat(result.getId()).isNull();
            assertThat(result.getRoomId()).isNull();
        }

        @Test
        @DisplayName("Mapea CreateUserRequest con phone null")
        void mapsRequestWithNullPhone() {
            CreateUserRequest request = CreateUserRequest.builder()
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone(null)
                    .rol("admin")
                    .build();

            UserDTO result = UserRestMapper.toApplicationDTO(request);

            assertThat(result).isNotNull();
            assertThat(result.getPhone()).isNull();
            assertThat(result.getName()).isEqualTo("pablo");
        }

        @Test
        @DisplayName("Maneja null input devolviendo null")
        void handlesNullInput() {
            UserDTO result = UserRestMapper.toApplicationDTO(null);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("No mapea id y roomId (no vienen en request)")
        void doesNotMapIdAndRoomId() {
            CreateUserRequest request = CreateUserRequest.builder()
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone(null)
                    .rol("admin")
                    .build();

            UserDTO result = UserRestMapper.toApplicationDTO(request);

            assertThat(result.getId()).isNull();
            assertThat(result.getRoomId()).isNull();
        }
    }

    @Nested
    @DisplayName("toHttpResponse - Application DTO → CreateUserResponse")
    class ToHttpResponseTests {

        @Test
        @DisplayName("Mapea UserDTO completo a CreateUserResponse")
        void mapsCompleteUserDtoToResponse() {
            UserDTO userDto = UserDTO.builder()
                    .id(42L)
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone("677998899")
                    .rol("ADMIN")
                    .roomId(1L)
                    .build();

            CreateUserResponse result = UserRestMapper.toHttpResponse(userDto);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(42L);
            assertThat(result.getName()).isEqualTo("pablo");
            assertThat(result.getEmail()).isEqualTo("pablo@example.com");
            assertThat(result.getRol()).isEqualTo("ADMIN");
            assertThat(result.getRoomId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("No mapea dni y phone en response")
        void doesNotMapDniAndPhone() {
            UserDTO userDto = UserDTO.builder()
                    .id(42L)
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone("677998899")
                    .rol("ADMIN")
                    .roomId(1L)
                    .build();

            CreateUserResponse result = UserRestMapper.toHttpResponse(userDto);

            // CreateUserResponse no tiene dni ni phone, solo verifica que se crea correctamente
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(42L);
        }

        @Test
        @DisplayName("Maneja null input devolviendo null")
        void handlesNullInput() {
            CreateUserResponse result = UserRestMapper.toHttpResponse(null);
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("toGetUserResponse - Application DTO → GetUserResponse")
    class ToGetUserResponseTests {

        @Test
        @DisplayName("Mapea UserDTO completo a GetUserResponse")
        void mapsCompleteUserDtoToGetResponse() {
            UserDTO userDto = UserDTO.builder()
                    .id(42L)
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone("677998899")
                    .rol("ADMIN")
                    .roomId(1L)
                    .build();

            GetUserResponse result = UserRestMapper.toGetUserResponse(userDto);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(42L);
            assertThat(result.getName()).isEqualTo("pablo");
            assertThat(result.getEmail()).isEqualTo("pablo@example.com");
            assertThat(result.getDni()).isEqualTo("23454234W");
            assertThat(result.getPhone()).isEqualTo("677998899");
            assertThat(result.getRol()).isEqualTo("ADMIN");
            assertThat(result.getRoomId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Mapea UserDTO con phone null")
        void mapsUserDtoWithNullPhone() {
            UserDTO userDto = UserDTO.builder()
                    .id(42L)
                    .name("pablo")
                    .email("pablo@example.com")
                    .dni("23454234W")
                    .phone(null)
                    .rol("ADMIN")
                    .roomId(1L)
                    .build();

            GetUserResponse result = UserRestMapper.toGetUserResponse(userDto);

            assertThat(result).isNotNull();
            assertThat(result.getPhone()).isNull();
            assertThat(result.getDni()).isEqualTo("23454234W");
        }

        @Test
        @DisplayName("Maneja null input devolviendo null")
        void handlesNullInput() {
            GetUserResponse result = UserRestMapper.toGetUserResponse(null);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Mapea todos los campos incluido DNI")
        void mapsAllFieldsIncludingDni() {
            UserDTO userDto = UserDTO.builder()
                    .id(99L)
                    .name("juan")
                    .email("juan@example.com")
                    .dni("12345678A")
                    .phone("666777888")
                    .rol("SUPERADMIN")
                    .roomId(2L)
                    .build();

            GetUserResponse result = UserRestMapper.toGetUserResponse(userDto);

            assertThat(result.getId()).isEqualTo(99L);
            assertThat(result.getName()).isEqualTo("juan");
            assertThat(result.getEmail()).isEqualTo("juan@example.com");
            assertThat(result.getDni()).isEqualTo("12345678A");
            assertThat(result.getPhone()).isEqualTo("666777888");
            assertThat(result.getRol()).isEqualTo("SUPERADMIN");
            assertThat(result.getRoomId()).isEqualTo(2L);
        }
    }
}


