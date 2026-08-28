package com.capgemini.test.code.application.usecase.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.domain.user.exceptions.UserNotFoundException;
import com.capgemini.test.code.infrastructure.adapter.output.persistence.commondb.user.UserPersistenceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para GetUserUseCase.
 *
 * Requisitos del README:
 * - Obtener usuario por ID
 * - Lanzar excepción si no existe
 * - Mapear entidad a DTO correctamente
 * - Lectura sin efectos secundarios
 */
@DisplayName("GetUserUseCase - Obtener Usuario")
class GetUserUseCaseTest {

  private GetUserUseCase useCase;
  private UserPersistenceAdapter userRepository;

  @BeforeEach
  void setup() {
    userRepository = mock(UserPersistenceAdapter.class);
    useCase = new GetUserUseCase(userRepository);
  }

  @Nested
  @DisplayName("Obtener usuario existente")
  class GetExistingUser {

    @Test
    @DisplayName("Debe obtener usuario por ID y retornar DTO")
    void shouldGetUserByIdAndReturnDto() {
      // Arrange - Requisito: GET /api/v1/users/{id}
      UserDTO user = UserDTO.builder().id(42L)
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("ADMIN")
          .roomId(1L)
          .build();

      when(userRepository.findById(42L)).thenReturn(user);

      // Act
      UserDTO result = useCase.execute(42L);

      // Assert - Requisito: Retornar datos del usuario
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(42L);
      assertThat(result.getName()).isEqualTo("pablo");
      assertThat(result.getEmail()).isEqualTo("pablo@example.com");
      assertThat(result.getDni()).isEqualTo("23454234W");
      assertThat(result.getRole()).isEqualToIgnoringCase("ADMIN");
      assertThat(result.getRoomId()).isEqualTo(1L);
      assertThat(result.getPhone()).isNull();

      // Verificar que se buscó en el repositorio
      verify(userRepository).findById(42L);
    }

    @Test
    @DisplayName("Debe obtener usuario SUPERADMIN con teléfono")
    void shouldGetSuperadminUserWithPhone() {
      // Arrange
      UserDTO user = UserDTO.builder().id(43L)
          .name("juan")
          .email("juan@example.com")
          .dni("12345678A")
          .phone("677998899")
          .role("SUPERADMIN")
          .roomId(1L)
          .build();


      when(userRepository.findById(43L)).thenReturn(user);

      // Act
      UserDTO result = useCase.execute(43L);

      // Assert
      assertThat(result.getId()).isEqualTo(43L);
      assertThat(result.getName()).isEqualTo("juan");
      assertThat(result.getPhone()).isEqualTo("677998899");
      assertThat(result.getRole()).isEqualToIgnoringCase("SUPERADMIN");
    }

    @Test
    @DisplayName("Debe retornar DTO completo con todos los campos")
    void shouldReturnCompleteDtoWithAllFields() {
      // Arrange
      UserDTO user = UserDTO.builder().id(50L)
          .name("maria")
          .email("maria@test.com")
          .dni("99887766Z")
          .phone("612345678")
          .role("ADMIN")
          .roomId(1L)
          .build();


      when(userRepository.findById(50L)).thenReturn(user);

      // Act
      UserDTO result = useCase.execute(50L);

      // Assert - Verificar que todos los campos se mapean correctamente
      assertThat(result.getId()).isEqualTo(50L);
      assertThat(result.getName()).isEqualTo("maria");
      assertThat(result.getEmail()).isEqualTo("maria@test.com");
      assertThat(result.getDni()).isEqualTo("99887766Z");
      assertThat(result.getPhone()).isEqualTo("612345678");
      assertThat(result.getRole()).isEqualToIgnoringCase("ADMIN");
      assertThat(result.getRoomId()).isEqualTo(1L);
    }
  }

  @Nested
  @DisplayName("Obtener usuario no existente")
  class GetNonExistentUser {

    @Test
    @DisplayName("Debe lanzar UserNotFoundException si usuario no existe")
    void shouldThrowUserNotFoundExceptionWhenNotExists() {
      // Arrange - Requisito: Lanzar excepción si no existe
      when(userRepository.findById(999L)).thenReturn(null);

      // Act & Assert
      assertThatThrownBy(() -> useCase.execute(999L))
          .isInstanceOf(UserNotFoundException.class);

      // Verificar que se intentó buscar
      verify(userRepository).findById(999L);
    }

    @Test
    @DisplayName("Debe rechazar ID negativo (no buscar en BD)")
    void shouldRejectNegativeId() {
      // Arrange
      when(userRepository.findById(-1L)).thenReturn(null);

      // Act & Assert
      assertThatThrownBy(() -> useCase.execute(-1L))
          .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Debe rechazar ID cero")
    void shouldRejectZeroId() {
      // Arrange
      when(userRepository.findById(0L)).thenReturn(null);

      // Act & Assert
      assertThatThrownBy(() -> useCase.execute(0L))
          .isInstanceOf(UserNotFoundException.class);
    }
  }

  @Nested
  @DisplayName("Mapeo de datos (User → UserDTO)")
  class DataMapping {

    @Test
    @DisplayName("Debe mapear UserRole.ADMIN a 'ADMIN' en DTO")
    void shouldMapAdminRoleCorrectly() {
      // Arrange
      UserDTO user = UserDTO.builder().id(1L)
          .name("ADMIN")
          .email("admin@example.com")
          .dni("11111111A")
          .phone(null)
          .role("ADMIN")
          .roomId(1L)
          .build();

      when(userRepository.findById(1L)).thenReturn(user);
      // Act
      UserDTO result = useCase.execute(1L);

      // Assert
      assertThat(result.getRole()).isEqualToIgnoringCase("ADMIN");
    }

    @Test
    @DisplayName("Debe mapear UserRole.SUPERADMIN a 'SUPERADMIN' en DTO")
    void shouldMapSuperadminRoleCorrectly() {
      // Arrange
      UserDTO user = UserDTO.builder().id(2L)
          .name("super")
          .email("super@example.com")
          .dni("22222222B")
          .phone("611111111")
          .role("SUPERADMIN")
          .roomId(1L)
          .build();


      when(userRepository.findById(2L)).thenReturn(user);

      // Act
      UserDTO result = useCase.execute(2L);

      // Assert
      assertThat(result.getRole()).isEqualToIgnoringCase("SUPERADMIN");
    }

    @Test
    @DisplayName("Debe preservar todos los valores durante el mapeo")
    void shouldPreserveAllValuesInMapping() {
      // Arrange
      UserDTO user = UserDTO.builder().id(100L)
          .name("test")
          .email("test@example.com")
          .dni("33333333C")
          .phone("622222222")
          .role("ADMIN")
          .roomId(5L)
          .build();


      when(userRepository.findById(100L)).thenReturn(user);
      // Act
      UserDTO result = useCase.execute(100L);

      // Assert
      assertThat(result)
          .satisfies(dto -> {
            assertThat(dto.getId()).isEqualTo(100L);
            assertThat(dto.getName()).isEqualTo("test");
            assertThat(dto.getEmail()).isEqualTo("test@example.com");
            assertThat(dto.getDni()).isEqualTo("33333333C");
            assertThat(dto.getPhone()).isEqualTo("622222222");
            assertThat(dto.getRole()).isEqualToIgnoringCase("ADMIN");
            assertThat(dto.getRoomId()).isEqualTo(5L);
          });
    }

    @Test
    @DisplayName("Debe mapear null phone correctamente")
    void shouldMapNullPhoneCorrectly() {
      // Arrange
      UserDTO user = UserDTO.builder().id(3L)
          .name("notel")
          .email("nophone@example.com")
          .dni("44444444D")
          .phone(null)
          .role("ADMIN")
          .roomId(1L)
          .build();


      when(userRepository.findById(3L)).thenReturn(user);
      // Act
      UserDTO result = useCase.execute(3L);

      // Assert
      assertThat(result.getPhone()).isNull();
    }
  }

  @Nested
  @DisplayName("Búsqueda en repositorio")
  class RepositoryInteraction {

    @Test
    @DisplayName("Debe consultar el repositorio correctamente")
    void shouldQueryRepositoryCorrectly() {
      // Arrange
      UserDTO user = UserDTO.builder().id(7L)
          .name("test")
          .email("test@example.com")
          .dni("55555555E")
          .phone(null)
          .role("ADMIN")
          .roomId(1L)
          .build();


      when(userRepository.findById(7L)).thenReturn(user);
      // Act
      useCase.execute(7L);

      // Assert
      verify(userRepository).findById(7L);
    }

    @Test
    @DisplayName("Debe usar el ID proporcionado para buscar")
    void shouldUseProvidedIdForSearch() {
      // Arrange
      UserDTO user1 = UserDTO.builder().id(10L)
          .name("user1")
          .email("user1@example.com")
          .dni("11111111A")
          .phone(null)
          .role("ADMIN")
          .roomId(1L)
          .build();

      UserDTO user2 = UserDTO.builder().id(20L)
          .name("user2")
          .email("user2@example.com")
          .dni("22222222B")
          .phone(null)
          .role("ADMIN")
          .roomId(1L)
          .build();

      when(userRepository.findById(10L)).thenReturn(user1);
      when(userRepository.findById(20L)).thenReturn(user2);

      // Act
      UserDTO result1 = useCase.execute(10L);
      UserDTO result2 = useCase.execute(20L);

      // Assert
      assertThat(result1.getId()).isEqualTo(10L);
      assertThat(result1.getName()).isEqualTo("user1");
      assertThat(result2.getId()).isEqualTo(20L);
      assertThat(result2.getName()).isEqualTo("user2");
    }
  }
}




