package br.com.flaviohblima.rinhabackend3.payments_summary;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SummaryResponse(
        @JsonProperty("default") ProcessorSummary defaultProcessor,
        @JsonProperty("fallback") ProcessorSummary fallbackProcessor) {
}
