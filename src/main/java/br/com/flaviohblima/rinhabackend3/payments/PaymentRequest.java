package br.com.flaviohblima.rinhabackend3.payments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest(@NotBlank String correlationId, @Positive BigDecimal amount) {

}
