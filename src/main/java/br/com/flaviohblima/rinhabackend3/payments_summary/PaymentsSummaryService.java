package br.com.flaviohblima.rinhabackend3.payments_summary;

import br.com.flaviohblima.rinhabackend3.payments_repository.PaymentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PaymentsSummaryService {

    private static final Logger log = LoggerFactory.getLogger(PaymentsSummaryService.class);

    private final PaymentsRepository repository;

    @Autowired
    public PaymentsSummaryService(PaymentsRepository repository) {
        this.repository = repository;
    }

    public SummaryResponse getSummary(Instant from, Instant to) {
        log.debug("from: {} -> to: {}", from, to);
        ProcessorSummary defaultProcessorSummary = repository
                .countAndSumByProcessorNameAndIsProcessedAndRequestedAtBetween("default", from, to);
        ProcessorSummary fallbackProcessorSummary = repository
                .countAndSumByProcessorNameAndIsProcessedAndRequestedAtBetween("fallback", from, to);
        return new SummaryResponse(defaultProcessorSummary, fallbackProcessorSummary);
    }
}
