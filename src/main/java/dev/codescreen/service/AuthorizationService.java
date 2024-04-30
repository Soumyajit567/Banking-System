package dev.codescreen.service;

import dev.codescreen.model.AuthorizationRequest;
import dev.codescreen.model.AuthorizationResponse;
import dev.codescreen.repository.AuthorizationRequestRepository;
import dev.codescreen.repository.AuthorizationResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuthorizationService {

    @Autowired
    private AuthorizationRequestRepository requestRepository;

    @Autowired
    private AuthorizationResponseRepository responseRepository;

    public AuthorizationResponse authorize(AuthorizationRequest request) {
        /**
         * Save the request
         */
        requestRepository.save(request);

        /**
         * Business logic to check funds, etc.
         */
        boolean isAuthorized = checkFunds(request.getUserId(), request.getAmount());

        /** 
         * Preparing response
         */
        AuthorizationResponse response = new AuthorizationResponse();
        response.setUserId(request.getUserId());
        response.setMessageId(request.getMessageId());
        response.setCurrency(request.getCurrency());
        response.setDebitOrCredit(request.getDebitOrCredit());
        response.setResponseCode(isAuthorized ? "APPROVED" : "DECLINED");
        response.setBalance(calculateNewBalance(request.getUserId(), request.getAmount(), isAuthorized));

        /** 
         * Saving and returning response
         */
        responseRepository.save(response);
        return response;
    }

    private boolean checkFunds(String userId, BigDecimal amount) {
        
        return true; 
    }

    private BigDecimal calculateNewBalance(String userId, BigDecimal amount, boolean isAuthorized) {
        /** 
         * Implementing balance calculation
         */
        return new BigDecimal("1000.00"); 
    }
}
