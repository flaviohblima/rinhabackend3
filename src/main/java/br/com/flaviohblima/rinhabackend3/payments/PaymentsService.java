package br.com.flaviohblima.rinhabackend3.payments;

import br.com.flaviohblima.rinhabackend3.payments_processor.IPaymentProcessorClient;
import br.com.flaviohblima.rinhabackend3.payments_processor.PaymentPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PaymentsService {

    private static final Logger log = LoggerFactory.getLogger(PaymentsService.class);

    private final IPaymentProcessorClient processorClient;

    public PaymentsService(IPaymentProcessorClient processorClient) {
        this.processorClient = processorClient;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        log.debug("{}", request);
        processorClient.processPayment(new PaymentPayload(request.correlationId(), request.amount(), Instant.now()));
        return new PaymentResponse("Payment processed!");
    }
}
