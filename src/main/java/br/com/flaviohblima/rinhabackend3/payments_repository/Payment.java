package br.com.flaviohblima.rinhabackend3.payments_repository;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    private UUID correlationId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessorName processorName = ProcessorName.DEFAULT;

    @Column(nullable = false)
    private boolean isProcessed = false;

    @Column(nullable = false, updatable = false)
    private Instant requestedAt = Instant.now();

    public Payment() {
    }

    public Payment(String correlationId, BigDecimal amount) {
        this.correlationId = UUID.fromString(correlationId);
        this.amount = amount;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ProcessorName getProcessorName() {
        return processorName;
    }

    public void setProcessorName(ProcessorName processorName) {
        this.processorName = processorName;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean isProcessed) {
        this.isProcessed = isProcessed;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Instant requestedAt) {
        this.requestedAt = requestedAt;
    }
}
