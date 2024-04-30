package dev.codescreen.Controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import dev.codescreen.model.AuthorizationRequest;
import dev.codescreen.model.AuthorizationResponse;
import dev.codescreen.model.LoadRequest;
import dev.codescreen.model.LoadResponse;
import dev.codescreen.service.TransactionService;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Collections.singletonMap("serverTime", LocalDateTime.now().toString());
    }


    @PostMapping("/authorization")
    public ResponseEntity<AuthorizationResponse> authorize(@RequestBody AuthorizationRequest request) {
        try {
            System.out.println("Received AuthorizationRequest: userId=" + request.getUserId() +
                            ", messageId=" + request.getMessageId() +
                            ", amount=" + request.getAmount() +
                            ", currency=" + request.getCurrency() +
                            ", debitOrCredit=" + request.getDebitOrCredit());

            AuthorizationResponse response = transactionService.authorize(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error processing authorization: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    


    @PostMapping("/load")
    public ResponseEntity<?> load(@RequestBody LoadRequest request) {
        if (request.getAmount() == null) {
            System.out.println("Error: Received load request with null amount");
            return ResponseEntity.badRequest().body("Error: Amount must not be null for load operations");
        }
        System.out.println("Received load request: " + request);
        LoadResponse response = transactionService.load(request);
        return ResponseEntity.ok(response);
    }
}
