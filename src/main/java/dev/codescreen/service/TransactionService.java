package dev.codescreen.service;

import dev.codescreen.model.*;
import dev.codescreen.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AuthorizationRequestRepository authorizationRequestRepository;
    private final AuthorizationResponseRepository authorizationResponseRepository;
    private final LoadRequestRepository loadRequestRepository;
    private final LoadResponseRepository loadResponseRepository;
    private final AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TransactionService(TransactionRepository transactionRepository,
                              AuthorizationRequestRepository authorizationRequestRepository,
                              AuthorizationResponseRepository authorizationResponseRepository,
                              LoadRequestRepository loadRequestRepository,
                              LoadResponseRepository loadResponseRepository,
                              AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.authorizationRequestRepository = authorizationRequestRepository;
        this.authorizationResponseRepository = authorizationResponseRepository;
        this.loadRequestRepository = loadRequestRepository;
        this.loadResponseRepository = loadResponseRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AuthorizationResponse authorize(AuthorizationRequest request) {
        AuthorizationResponse response = new AuthorizationResponse();
        response.setUserId(request.getUserId());
        response.setMessageId(request.getMessageId());
        response.setCurrency(request.getCurrency());
        response.setDebitOrCredit(request.getDebitOrCredit());

        Account account = accountRepository.findByUserId(request.getUserId());
        if (account == null) {
            account = new Account();
            account.setUserId(request.getUserId());
            account.setBalance(BigDecimal.ZERO);

        /**
          * Creating a new account if it does not exist
          */

            accountRepository.save(account); 
        } else {
        
        /** 
         * Refresh the entity to get the latest from the database
         */
            entityManager.refresh(account); 
        }

        BigDecimal currentBalance = account.getBalance();
        System.out.println("Current Balance: " + currentBalance + ", Requested: " + request.getAmount());

        BigDecimal newBalance = calculateNewBalance(currentBalance, request);
        if (newBalance != null) {
            account.setBalance(newBalance);
            response.setBalance(newBalance);
            response.setResponseCode("APPROVED");
        } else {
            response.setResponseCode("DENIED");
            response.setErrorMessage("Insufficient funds or invalid amount");
            response.setBalance(currentBalance);
        }

        authorizationResponseRepository.save(response);
        return response;
    }

    private BigDecimal calculateNewBalance(BigDecimal currentBalance, AuthorizationRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        return "CREDIT".equals(request.getDebitOrCredit()) ? currentBalance.add(request.getAmount()) :
               (currentBalance.compareTo(request.getAmount()) >= 0 ? currentBalance.subtract(request.getAmount()) : null);
    }

    
    @Transactional
    public LoadResponse load(LoadRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
        loadRequestRepository.save(request);
        if ("DEBIT".equals(request.getDebitOrCredit())) {
            throw new IllegalArgumentException("Load operation cannot be a DEBIT");
        }

        Account account = accountRepository.findByUserId(request.getUserId());
        if (account == null) {
            account = new Account();
            account.setUserId(request.getUserId());
            account.setBalance(BigDecimal.ZERO);
        }
        
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = calculateNewBalanceForLoad(currentBalance, request.getAmount());
        
        account.setBalance(newBalance);
        accountRepository.save(account);
        entityManager.flush(); 

        LoadResponse response = new LoadResponse();
        response.setUserId(request.getUserId());
        response.setMessageId(request.getMessageId());
        response.setBalance(newBalance);
        response.setCurrency(request.getCurrency());
        response.setDebitOrCredit(request.getDebitOrCredit());

        loadResponseRepository.save(response);
        return response;
    }

    private BigDecimal calculateNewBalanceForLoad(BigDecimal currentBalance, BigDecimal amount) {
        return currentBalance.add(amount);
    }
}
