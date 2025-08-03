package br.com.flaviohblima.rinhabackend3.payments_repository;

import br.com.flaviohblima.rinhabackend3.payments_summary.ProcessorSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, String> {

    @Query("SELECT " +
            "   count(p) AS totalRequests," +
            "   sum(p.amount) AS totalAmount" +
            " FROM Payment p " +
            " WHERE p.isProcessed = true " +
            "   AND p.processorName = :processorName " +
            "   AND requestedAt BETWEEN :from AND :to")
    ProcessorSummary countAndSumByProcessorNameAndIsProcessedAndRequestedAtBetween(
            @Param("processorName") String processorName,
            @Param("from") Instant from, @Param("to") Instant to);

}
