package br.com.flaviohblima.rinhabackend3.payments_processor;

import java.math.BigDecimal;

public record PaymentPayload(String correlationId, BigDecimal amount, String requestedAt) {

}
