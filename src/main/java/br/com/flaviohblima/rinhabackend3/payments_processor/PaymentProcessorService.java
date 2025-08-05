package br.com.flaviohblima.rinhabackend3.payments_processor;

import br.com.flaviohblima.rinhabackend3.payments_repository.Payment;
import br.com.flaviohblima.rinhabackend3.payments_repository.PaymentsRepository;
import br.com.flaviohblima.rinhabackend3.payments_repository.ProcessorName;
import br.com.flaviohblima.rinhabackend3.queue_service.QueueService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessorService {

    private static final Logger log = LoggerFactory.getLogger(PaymentProcessorService.class);
    private final QueueService queueService;
    private final PaymentsRepository repository;
    private final IPaymentProcessorClient processorClient;

    public PaymentProcessorService(QueueService queueService,
                                   PaymentsRepository repository,
                                   IPaymentProcessorClient processorClient) {
        this.queueService = queueService;
        this.repository = repository;
        this.processorClient = processorClient;
    }

    @CircuitBreaker(name = "process-payment", fallbackMethod = "callPaymentProcessorFallback")
    public void callPaymentProcessor(Payment payment) {
        log.debug("callPaymentProcessor {}", payment);
        processorClient.processPayment(new PaymentPayload(payment));

        payment.setProcessed(true);
        payment.setProcessorName(ProcessorName.DEFAULT);
        repository.save(payment);
    }

    public void callPaymentProcessorFallback(Payment payment, Throwable t) {
        log.debug("callPaymentProcessorFallback {}", payment);
        try {
            processorClient.processPaymentFallback(new PaymentPayload(payment));

            payment.setProcessed(true);
            payment.setProcessorName(ProcessorName.FALLBACK);
            repository.save(payment);
        } catch (RuntimeException ex) {
            enqueuePaymentForRetry(payment, ex);
        }
    }

    public void enqueuePaymentForRetry(Payment payment, Throwable t) {
        log.debug("enqueuePaymentForRetry {}", payment);
        payment.setProcessed(false);
        repository.save(payment);
        queueService.enqueueForRetry(payment.getCorrelationId().toString());
    }

}
