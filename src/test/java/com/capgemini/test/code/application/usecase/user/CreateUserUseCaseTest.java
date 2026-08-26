package com.capgemini.test.code.application.usecase.user;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.application.ports.output.DniValidationPort;
import com.capgemini.test.code.application.ports.output.NotificationPort;
import com.capgemini.test.code.domain.user.exceptions.DuplicateEmailException;
import com.capgemini.test.code.domain.user.exceptions.InvalidDniException;
import com.capgemini.test.code.domain.user.exceptions.InvalidEmailException;
import com.capgemini.test.code.domain.user.exceptions.InvalidPhoneException;
import com.capgemini.test.code.domain.user.exceptions.InvalidRoleException;
import com.capgemini.test.code.domain.user.exceptions.InvalidUserNameException;
import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para CreateUserUseCase.
 *
 * Requisitos del README:
 * - Crear usuario con datos válidos
 * - Validar DNI contra API externa
 * - Rechazar email duplicado
 */
@DisplayName("CreateUserUseCase - Casos de Uso")
class CreateUserUseCaseTest {

  private CreateUserUseCase useCase;
  private UserRepository userRepository;
  private DniValidationPort dniValidationPort;
  private NotificationPort notificationPort;

  @BeforeEach
  void setup() {
    userRepository = mock(UserRepository.class);
    dniValidationPort = mock(DniValidationPort.class);
    notificationPort = mock(NotificationPort.class);
    useCase = new CreateUserUseCase(userRepository, dniValidationPort, notificationPort);
  }

  @Nested
  @DisplayName("Crear usuario válido")
  class CreateValidUser {

    @Test
    @DisplayName("Debe crear usuario ADMIN con datos válidos")
    void shouldCreateValidAdminUser() {
      UserDTO requestDTO = UserDTO.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .rol("admin")
          .build();

      User savedUser = User.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .role("admin")
          .roomId(1L)
          .buildWithId(42L);

      doNothing().when(dniValidationPort).validate("23454234W");
      when(userRepository.existsByEmail("pablo@example.com")).thenReturn(false);
      when(userRepository.save(any(User.class))).thenReturn(savedUser);

      UserDTO result = useCase.execute(requestDTO);

      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(42L);
      assertThat(result.getName()).isEqualTo("pablo");
      assertThat(result.getRol()).isEqualTo("ADMIN");
      verify(userRepository).save(any(User.class));
      verify(notificationPort).notifyUserCreated(any(User.class));
    }

    @Test
    @DisplayName("Debe crear usuario SUPERADMIN con datos válidos")
    void shouldCreateValidSuperadminUser() {
      UserDTO requestDTO = UserDTO.builder()
          .name("juan")
          .email("juan@example.com")
          .dni("12345678A")
          .phone("677998899")
          .rol("superadmin")
          .build();

      User savedUser = User.builder()
          .name("juan")
          .email("juan@example.com")
          .dni("12345678A")
          .phone("677998899")
          .role("superadmin")
          .roomId(1L)
          .buildWithId(43L);

      doNothing().when(dniValidationPort).validate("12345678A");
      when(userRepository.existsByEmail("juan@example.com")).thenReturn(false);
      when(userRepository.save(any(User.class))).thenReturn(savedUser);

      UserDTO result = useCase.execute(requestDTO);

      assertThat(result.getId()).isEqualTo(43L);
      assertThat(result.getRol()).isEqualTo("SUPERADMIN");
      verify(notificationPort).notifyUserCreated(any(User.class));
    }
  }

  @Nested
  @DisplayName("Validaciones de dominio")
  class DomainValidations {

    @Test
    @DisplayName("Debe rechazar nombre > 6 caracteres")
    void shouldRejectNameExceeding6Chars() {
      UserDTO requestDTO = UserDTO.builder()
          .name("pablogarcia")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .rol("admin")
          .build();

      assertThatThrownBy(() -> useCase.execute(requestDTO))
          .isInstanceOf(InvalidUserNameException.class);
      verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar email sin @")
    void shouldRejectEmailWithoutAtSymbol() {
      UserDTO requestDTO = UserDTO.builder()
          .name("pablo")
          .email("pabloemail.com")
          .dni("23454234W")
          .phone(null)
          .rol("admin")
          .build();

      assertThatThrownBy(() -> useCase.execute(requestDTO))
          .isInstanceOf(InvalidEmailException.class);
      verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe rechazar rol inválido")
    void shouldRejectInvalidRole() {
      UserDTO requestDTO = UserDTO.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .rol("user")
          .build();

      assertThatThrownBy(() -> useCase.execute(requestDTO))
          .isInstanceOf(InvalidRoleException.class);
      verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe requerir teléfono para SUPERADMIN")
    void shouldRequirePhoneForSuperadmin() {
      UserDTO requestDTO = UserDTO.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .rol("superadmin")
          .build();

      assertThatThrownBy(() -> useCase.execute(requestDTO))
          .isInstanceOf(InvalidPhoneException.class);
      verify(userRepository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("Validación de DNI (API externa)")
  class DniValidation {

    @Test
    @DisplayName("Debe validar DNI contra API externa")
    void shouldValidateDniWithExternalApi() {
      UserDTO requestDTO = UserDTO.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .rol("admin")
          .build();

      doNothing().when(dniValidationPort).validate("23454234W");
      when(userRepository.existsByEmail("pablo@example.com")).thenReturn(false);
      when(userRepository.save(any(User.class))).thenReturn(
          User.builder()
              .name("pablo")
              .email("pablo@example.com")
              .dni("23454234W")
              .phone(null)
              .role("admin")
              .roomId(1L)
              .buildWithId(1L)
      );

      useCase.execute(requestDTO);

      verify(dniValidationPort).validate("23454234W");
    }

    @Test
    @DisplayName("Debe rechazar DNI 99999999W")
    void shouldRejectBlacklistedDni() {
      UserDTO requestDTO = UserDTO.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("99999999W")
          .phone(null)
          .rol("admin")
          .build();

      doThrow(new InvalidDniException("DNI inválido")).when(dniValidationPort).validate("99999999W");

      assertThatThrownBy(() -> useCase.execute(requestDTO))
          .isInstanceOf(InvalidDniException.class);

      verify(dniValidationPort).validate("99999999W");
      verify(userRepository, never()).save(any());
      verify(notificationPort, never()).notifyUserCreated(any());
    }
  }

  @Nested
  @DisplayName("Validación de duplicados")
  class DuplicateValidation {

    @Test
    @DisplayName("Debe rechazar email duplicado")
    void shouldRejectDuplicateEmail() {
      UserDTO requestDTO = UserDTO.builder()
          .name("pablo")
          .email("duplicate@example.com")
          .dni("23454234W")
          .phone(null)
          .rol("admin")
          .build();

      doNothing().when(dniValidationPort).validate("23454234W");
      when(userRepository.existsByEmail("duplicate@example.com")).thenReturn(true);

      assertThatThrownBy(() -> useCase.execute(requestDTO))
          .isInstanceOf(DuplicateEmailException.class);

      verify(dniValidationPort).validate("23454234W");
      verify(userRepository, never()).save(any());
      verify(notificationPort, never()).notifyUserCreated(any());
    }
  }

  @Nested
  @DisplayName("Orden de ejecución (fail-fast)")
  class ExecutionOrder {

    @Test
    @DisplayName("Debe validar dominio ANTES de validar DNI")
    void shouldValidateDomainBeforeDni() {
      UserDTO requestDTO = UserDTO.builder()
          .name("pablogarcia")
          .email("pablo@example.com")
          .dni("23454234W")
          .phone(null)
          .rol("admin")
          .build();

      assertThatThrownBy(() -> useCase.execute(requestDTO))
          .isInstanceOf(InvalidUserNameException.class);

      verify(dniValidationPort, never()).validate(anyString());
    }

    @Test
    @DisplayName("Debe validar DNI ANTES de guardar en BD")
    void shouldValidateDniBeforeSave() {
      UserDTO requestDTO = UserDTO.builder()
          .name("pablo")
          .email("pablo@example.com")
          .dni("99999999W")
          .phone(null)
          .rol("admin")
          .build();

      doThrow(new InvalidDniException("DNI inválido")).when(dniValidationPort).validate("99999999W");

      assertThatThrownBy(() -> useCase.execute(requestDTO))
          .isInstanceOf(InvalidDniException.class);

      verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe verificar duplicados ANTES de guardar en BD")
    void shouldCheckDuplicateBeforeSave() {
      UserDTO requestDTO = UserDTO.builder()
          .name("pablo")
          .email("duplicate@example.com")
          .dni("23454234W")
          .phone(null)
          .rol("admin")
          .build();

      doNothing().when(dniValidationPort).validate("23454234W");
      when(userRepository.existsByEmail("duplicate@example.com")).thenReturn(true);

      assertThatThrownBy(() -> useCase.execute(requestDTO))
          .isInstanceOf(DuplicateEmailException.class);

      verify(userRepository, never()).save(any());
    }
  }
}

