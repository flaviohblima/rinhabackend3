package br.com.flaviohblima.rinhabackend3.payments_processor;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class PaymentProcessorClient implements IPaymentProcessorClient {

    private static final Logger log = LoggerFactory.getLogger(PaymentProcessorClient.class);

    private final RestTemplate rest;
    private final URI defaultProcessorAddress;
    private final URI fallbackProcessorAddress;

    private final HttpHeaders headers = new HttpHeaders();

    @Autowired
    public PaymentProcessorClient(
            RestTemplate rest,
            @Value("${app.paymentProcessor.default}") String defaultProcessorAddress,
            @Value("${app.paymentProcessor.fallback}") String fallbackProcessorAddress) {
        this.rest = rest;

        log.trace("app.paymentProcessor.default: {}", defaultProcessorAddress);
        this.defaultProcessorAddress = URI.create(defaultProcessorAddress);

        log.trace("app.paymentProcessor.fallback: {}", fallbackProcessorAddress);
        this.fallbackProcessorAddress = URI.create(fallbackProcessorAddress);

        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public HealthResponse getServiceHealth() {
        URI uri = defaultProcessorAddress.resolve("/service-health");
        return rest.getForObject(uri, HealthResponse.class);
    }

    @Override
    public HealthResponse getFallbackServiceHealth() {
        URI uri = fallbackProcessorAddress.resolve("/service-health");
        return rest.getForObject(uri, HealthResponse.class);
    }

    @Override
    @CircuitBreaker(name = "process-payment", fallbackMethod = "processPaymentFallback")
    public void processPayment(PaymentPayload payment) {
        URI uri = defaultProcessorAddress.resolve("/payments");
        rest.postForEntity(uri, new HttpEntity<>(payment, headers), Object.class);
    }

    @Override
    @CircuitBreaker(name = "process-payment-fallback", fallbackMethod = "processPaymentEnqueue")
    public void processPaymentFallback(PaymentPayload payment, Throwable t) {
        URI uri = fallbackProcessorAddress.resolve("/payments");
        rest.postForEntity(uri, new HttpEntity<>(payment, headers), Object.class);
    }

    public void processPaymentEnqueue(PaymentPayload payment, Throwable t) {
        log.error(t.getMessage(), t);
        log.info("None of the processors are responding! Lets enqueue the payment.");
        log.debug("Payment to enqueue {}", payment);
    }
}
