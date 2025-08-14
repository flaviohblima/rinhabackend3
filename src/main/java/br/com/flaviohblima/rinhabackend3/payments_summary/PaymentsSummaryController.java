package br.com.flaviohblima.rinhabackend3.payments_summary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/payments-summary")
public class PaymentsSummaryController {

    private final PaymentsSummaryService service;

    public PaymentsSummaryController(PaymentsSummaryService service) {
        this.service = service;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<SummaryResponse> getSummary(@RequestParam Instant from,
                                                      @RequestParam Instant to) {
        return ResponseEntity.ok(service.getSummary(from, to));
    }

}
