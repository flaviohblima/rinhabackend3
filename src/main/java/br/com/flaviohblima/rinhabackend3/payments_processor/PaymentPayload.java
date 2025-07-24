package br.com.flaviohblima.rinhabackend3.payments_processor;

import org.springframework.aot.hint.annotation.Reflective;

import java.math.BigDecimal;

@Reflective
public record PaymentPayload(String correlationId, BigDecimal amount, String requestedAt) {

}
