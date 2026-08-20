package com.paylens.events;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentEvent(
        @NotBlank @Size(max = 100) String transactionId,
        @NotBlank @Size(max = 100) String eventId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency,
        @NotBlank @Size(max = 50) String paymentMethod,
        @NotBlank @Size(max = 50) String status,
        @Size(max = 100) String failureCode,
        Instant timestamp
) {
    public PaymentEvent {
        timestamp = timestamp == null ? Instant.now() : timestamp;
    }
}
