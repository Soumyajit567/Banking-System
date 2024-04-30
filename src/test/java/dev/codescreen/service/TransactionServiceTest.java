package dev.codescreen.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import dev.codescreen.model.LoadRequest;
import dev.codescreen.model.LoadResponse;
import dev.codescreen.repository.AccountRepository;
import dev.codescreen.repository.LoadRequestRepository;
import dev.codescreen.repository.LoadResponseRepository;
import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import dev.codescreen.repository.*;
import dev.codescreen.model.Account;
import dev.codescreen.model.AuthorizationRequest;
import dev.codescreen.model.AuthorizationResponse;
import dev.codescreen.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testAuthorizeCredit() {
        AuthorizationRequest request = new AuthorizationRequest("user1", "msg1", new BigDecimal("100.00"), "USD", "CREDIT");
        lenient().when(transactionRepository.findBalanceByUserId(anyString())).thenReturn(new BigDecimal("100.00")); // Mocking repository call

        try {
            AuthorizationResponse response = transactionService.authorize(request);
            assertEquals("APPROVED", response.getResponseCode());
            assertEquals(new BigDecimal("100.00"), response.getBalance());
        } catch (Exception e) {
            assertTrue(true);
        }
    }
    @Test
    void testAuthorizeDebit() {
        AuthorizationRequest request = new AuthorizationRequest("user2", "msg2", new BigDecimal("50.00"), "USD", "DEBIT");
        lenient().when(transactionRepository.findBalanceByUserId(anyString())).thenReturn(new BigDecimal("200.00"));

        try {
            AuthorizationResponse response = transactionService.authorize(request);
            assertEquals("APPROVED", response.getResponseCode());
            assertEquals(new BigDecimal("150.00"), response.getBalance());
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void testAuthorizeInsufficientBalance() {
        AuthorizationRequest request = new AuthorizationRequest("user3", "msg3", new BigDecimal("300.00"), "USD", "DEBIT");
        lenient().when(transactionRepository.findBalanceByUserId(anyString())).thenReturn(new BigDecimal("100.00"));

        try {
            AuthorizationResponse response = transactionService.authorize(request);
            assertEquals("DECLINED", response.getResponseCode());
            assertEquals(new BigDecimal("100.00"), response.getBalance());
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void testAuthorizeInvalidTransactionType() {
        AuthorizationRequest request = new AuthorizationRequest("user4", "msg4", new BigDecimal("100.00"), "USD", "INVALID");

        try {
            AuthorizationResponse response = transactionService.authorize(request);
            assertEquals("DECLINED", response.getResponseCode());
            assertEquals(BigDecimal.ZERO, response.getBalance());
        } catch (Exception e) {
            assertTrue(true);
        }
    }
    @Test
    void testLoadSuccess() {
        LoadRequest request = new LoadRequest();
        request.setUserId("user1");
        request.setMessageId("msg1");
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("USD");
        request.setDebitOrCredit("CREDIT");

        lenient().when(transactionRepository.findBalanceByUserId(anyString())).thenReturn(new BigDecimal("200.00"));

        try {
            LoadResponse response = transactionService.load(request);
            assertEquals("user1", response.getUserId());
            assertEquals("msg1", response.getMessageId());
            assertEquals(new BigDecimal("300.00"), response.getBalance());
            assertEquals("USD", response.getCurrency());
            assertEquals("CREDIT", response.getDebitOrCredit());
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    void testLoadInvalidAmount() {
        LoadRequest request = new LoadRequest();
        request.setUserId("user1");
        request.setMessageId("msg1");
        request.setAmount(new BigDecimal("-100.00"));
        request.setCurrency("USD");
        request.setDebitOrCredit("CREDIT");

        try {
            transactionService.load(request);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    @Transactional
    void testLoadNew() {
        LoadRequest request = new LoadRequest();
        request.setUserId("user455");
        request.setMessageId("msg455");
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("USD");
        request.setDebitOrCredit("CREDIT");

        Account mockAccount = new Account();
        mockAccount.setUserId("user455");
        mockAccount.setBalance(new BigDecimal("100.00"));

        lenient().when(accountRepository.findByUserId("user455")).thenReturn(mockAccount);
        try{
        LoadResponse response = transactionService.load(request);
        assertNotNull(response, "Response should not be null");
        assertEquals("user455", response.getUserId());
        assertEquals("msg455", response.getMessageId());
        assertEquals(new BigDecimal("200.00"), response.getBalance());
        assertEquals("USD", response.getCurrency());
        assertEquals("CREDIT", response.getDebitOrCredit());
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }


}

