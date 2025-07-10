package br.com.flaviohblima.rinhabackend3.payments_summary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class PaymentsSummaryService {

    private static final Logger log = LoggerFactory.getLogger(PaymentsSummaryService.class);

    public SummaryResponse getSummary(Instant from, Instant to) {
        log.debug("from: {} -> to: {}", from, to);
        ProcessorSummary defaultProcessorSummary = new ProcessorSummary(1L, new BigDecimal(2));
        ProcessorSummary fallbackProcessorSummary = new ProcessorSummary(3L, new BigDecimal(4));
        return new SummaryResponse(defaultProcessorSummary, fallbackProcessorSummary);
    }
}
