package br.com.flaviohblima.rinhabackend3.payments;

import br.com.flaviohblima.rinhabackend3.payments_processor.IPaymentProcessorClient;
import br.com.flaviohblima.rinhabackend3.payments_repository.Payment;
import br.com.flaviohblima.rinhabackend3.payments_repository.PaymentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentsService {

    private static final Logger log = LoggerFactory.getLogger(PaymentsService.class);

    private final IPaymentProcessorClient processorClient;
    private final PaymentsRepository repository;

    @Autowired
    public PaymentsService(IPaymentProcessorClient processorClient,
                           PaymentsRepository repository) {
        this.processorClient = processorClient;
        this.repository = repository;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        log.debug("{}", request);
        Payment payment = new Payment(request.correlationId(), request.amount());
        repository.save(payment);
//        processorClient.processPayment(new PaymentPayload(request.correlationId(), request.amount(), Instant.now().toString()));
        return new PaymentResponse("Payment request received!");
    }
}
