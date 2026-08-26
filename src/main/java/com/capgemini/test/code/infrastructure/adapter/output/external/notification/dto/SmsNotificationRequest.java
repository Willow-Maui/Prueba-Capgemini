package com.capgemini.test.code.infrastructure.adapter.output.external.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SmsNotificationRequest - DTO para envío de SMS
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsNotificationRequest {

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("message")
    private String message;
}

