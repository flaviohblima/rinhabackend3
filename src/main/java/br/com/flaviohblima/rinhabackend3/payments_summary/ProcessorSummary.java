package br.com.flaviohblima.rinhabackend3.payments_summary;

import java.math.BigDecimal;

public record ProcessorSummary(Long totalRequests, BigDecimal totalAmount) {
}
