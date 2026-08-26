package com.capgemini.test.code.infrastructure.adapter.output.external.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmailNotificationRequest - DTO para envío de email
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailNotificationRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("message")
    private String message;
}

