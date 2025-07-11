package br.com.flaviohblima.rinhabackend3.payments_processor;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentPayload(String correlationId, BigDecimal amount, Instant requestedAt) {
}
