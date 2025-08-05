package br.com.flaviohblima.rinhabackend3.payments_processor;

public interface IPaymentProcessorClient {

    HealthResponse getServiceHealth();

    HealthResponse getFallbackServiceHealth();

    void processPayment(PaymentPayload payment);

    void processPaymentFallback(PaymentPayload payment);

}
