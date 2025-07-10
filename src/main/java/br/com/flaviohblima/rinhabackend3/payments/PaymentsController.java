package br.com.flaviohblima.rinhabackend3.payments;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentsController {

    private final PaymentsService service;

    public PaymentsController(PaymentsService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody @Valid PaymentRequest request) {
        return ResponseEntity.ok(service.processPayment(request));
    }

}
