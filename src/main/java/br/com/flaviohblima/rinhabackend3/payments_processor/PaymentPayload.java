package br.com.flaviohblima.rinhabackend3.payments_processor;

import br.com.flaviohblima.rinhabackend3.payments_repository.Payment;
import org.springframework.aot.hint.annotation.Reflective;

import java.math.BigDecimal;

@Reflective
public record PaymentPayload(String correlationId, BigDecimal amount, String requestedAt) {

    public PaymentPayload(Payment payment) {
        this(payment.getCorrelationId().toString(), payment.getAmount(), payment.getRequestedAt().toString());
    }

}
