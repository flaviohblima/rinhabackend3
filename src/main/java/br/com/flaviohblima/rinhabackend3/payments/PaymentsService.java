package br.com.flaviohblima.rinhabackend3.payments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentsService {

    private static final Logger log = LoggerFactory.getLogger(PaymentsService.class);

    public PaymentResponse processPayment(PaymentRequest request) {
        log.debug("{}", request);
        return new PaymentResponse("Payment processed!");
    }
}
