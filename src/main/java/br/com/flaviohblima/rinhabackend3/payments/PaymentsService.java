package br.com.flaviohblima.rinhabackend3.payments;

import br.com.flaviohblima.rinhabackend3.payments_processor.PaymentProcessorService;
import br.com.flaviohblima.rinhabackend3.payments_repository.Payment;
import br.com.flaviohblima.rinhabackend3.payments_repository.PaymentsRepository;
import br.com.flaviohblima.rinhabackend3.queue_service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentsService {

    private static final Logger log = LoggerFactory.getLogger(PaymentsService.class);

    private final QueueService queueService;
    private final PaymentsRepository repository;
    private final PaymentProcessorService paymentProcessorService;

    @Autowired
    public PaymentsService(QueueService queueService,
                           PaymentsRepository repository,
                           PaymentProcessorService paymentProcessorService) {
        this.queueService = queueService;
        this.repository = repository;
        this.paymentProcessorService = paymentProcessorService;
    }

    public PaymentResponse receivePayment(PaymentRequest request) {
        log.debug("receivePayment {}", request);

        if (!repository.existsById(UUID.fromString(request.correlationId()))) {
            log.debug("Persist {}", request);
            repository.save(new Payment(request.correlationId(), request.amount()));
        }

        log.debug("Enqueue {}", request);
        queueService.enqueue(request.correlationId());

        return new PaymentResponse("Payment request received!");
    }

    @Scheduled(fixedDelay = 100) // ms
    public void processPayment() {
        String correlationId = queueService.dequeue();
        if (correlationId == null) return;

        log.debug("Dequeued {}", correlationId);

        log.debug("Find {}", correlationId);
        Optional<Payment> payment = repository.findById(UUID.fromString(correlationId));
        if (payment.isEmpty()) return;

        paymentProcessorService.callPaymentProcessor(payment.get());
    }

}
