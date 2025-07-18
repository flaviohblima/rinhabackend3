package br.com.flaviohblima.rinhabackend3.payments_processor;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentProcessorClient implements IPaymentProcessorClient {

    private final RestTemplate rest;
    private final String defaultProcessorAddress;
    private final String fallbackProcessorAddress;

    public PaymentProcessorClient(RestTemplate rest,
                                  @Value("app.paymentProcessor.default") String defaultProcessorAddress,
                                  @Value("app.paymentProcessor.fallback") String fallbackProcessorAddress) {
        this.rest = rest;
        this.defaultProcessorAddress = defaultProcessorAddress;
        this.fallbackProcessorAddress = fallbackProcessorAddress;
    }

    @Override
    public HealthResponse getServiceHealth() {
        return rest.getForObject(defaultProcessorAddress + "/service-health", HealthResponse.class);
    }

    @Override
    public HealthResponse getFallbackServiceHealth() {
        return rest.getForObject(fallbackProcessorAddress + "/service-health", HealthResponse.class);
    }

    @Override
    @CircuitBreaker(name = "process-payment", fallbackMethod = "processPaymentFallback")
    public void processPayment(PaymentPayload payment) {
        rest.postForLocation(defaultProcessorAddress + "/payments", payment);
    }

    @Override
    public void processPaymentFallback(PaymentPayload payment, Throwable t) {
        rest.postForLocation(fallbackProcessorAddress + "/payments", payment);
    }
}
