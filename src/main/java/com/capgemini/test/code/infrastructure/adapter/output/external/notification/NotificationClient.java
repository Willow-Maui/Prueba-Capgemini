package com.capgemini.test.code.infrastructure.adapter.output.external.notification;

import com.capgemini.test.code.infrastructure.adapter.output.external.notification.dto.EmailNotificationRequest;
import com.capgemini.test.code.infrastructure.adapter.output.external.notification.dto.SmsNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * NotificationClient - Cliente Feign para notificaciones (email y SMS)
 *
 * Comunica con API externa en puerto 1080 (mock-server)
 * Endpoints: POST /email, POST /sms
 */
@FeignClient(
    name = "notification",
    url = "${external.notification.url}",
    configuration = NotificationFeignConfig.class
)
public interface NotificationClient {

    @PostMapping("/email")
    void sendEmail(@RequestBody EmailNotificationRequest request);

    @PostMapping("/sms")
    void sendSms(@RequestBody SmsNotificationRequest request);
}

