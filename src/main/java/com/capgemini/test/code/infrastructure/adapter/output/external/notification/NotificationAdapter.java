package com.capgemini.test.code.infrastructure.adapter.output.external.notification;

import com.capgemini.test.code.application.dto.UserDTO;
import com.capgemini.test.code.application.ports.output.NotificationPort;
import com.capgemini.test.code.domain.user.model.User;
import com.capgemini.test.code.domain.user.model.UserRole;
import com.capgemini.test.code.infrastructure.adapter.output.external.notification.dto.EmailNotificationRequest;
import com.capgemini.test.code.infrastructure.adapter.output.external.notification.dto.SmsNotificationRequest;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * NotificationAdapter - Implementa NotificationPort
 *
 * Responsabilidades:
 * 1. Enviar notificaciones según el rol del usuario
 * 2. Admin → Email
 * 3. Superadmin → SMS
 * 4. Manejo de errores Feign
 *
 * Patrón: Adapter (infraestructura)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationAdapter implements NotificationPort {

    private final NotificationClient client;

    private static final String NOTIFICATION_MESSAGE = "usuario guardado";

    /**
     * Notifica al usuario por su rol
     * Admin → Email
     * Superadmin → SMS
     *
     * Lanza excepción si el envío falla después de reintentos
     */
    @Override
    public void notifyUserCreated(UserDTO user) {
        try {
            log.debug("Notifying user creation: {} ({})",
                user.getEmail(), user.getRole());

            if (UserRole.ADMIN.equals(user.getRole())) {
                notifyByEmail(user);
            } else if (UserRole.SUPERADMIN.equals(user.getRole())) {
                notifySms(user);
            } else {
                log.warn("Unknown role for notification: {}", user.getRole());
            }

            log.debug("User notification sent successfully: {}",
                user.getEmail());

        } catch (FeignException e) {
            log.error("Error sending notification", e);
            throw new RuntimeException("Failed to send notification: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error sending notification", e);
            throw new RuntimeException("Unexpected error sending notification", e);
        }
    }

    private void notifyByEmail(UserDTO user) {
        log.debug("Sending email notification to: {}", user.getEmail());

        EmailNotificationRequest request = EmailNotificationRequest.builder()
            .email(user.getEmail())
            .message(NOTIFICATION_MESSAGE)
            .build();

        client.sendEmail(request);
    }

    private void notifySms(UserDTO user) {
        log.debug("Sending SMS notification to: {}", user.getPhone());

        SmsNotificationRequest request = SmsNotificationRequest.builder()
            .phone(user.getPhone())
            .message(NOTIFICATION_MESSAGE)
            .build();

        client.sendSms(request);
    }
}

