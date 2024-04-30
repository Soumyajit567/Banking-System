package dev.codescreen.Controller;

import dev.codescreen.model.AuthorizationRequest;
import dev.codescreen.model.AuthorizationResponse;
import dev.codescreen.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {
    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/transactions/authorization")
    public ResponseEntity<AuthorizationResponse> authorize(@RequestBody AuthorizationRequest request) {
        try {
            AuthorizationResponse response = authorizationService.authorize(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}