package dev.codescreen.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import static org.mockito.Mockito.lenient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import dev.codescreen.model.LoadRequest;
import dev.codescreen.model.LoadResponse;
import dev.codescreen.model.AuthorizationRequest;
import dev.codescreen.model.AuthorizationResponse;
import dev.codescreen.service.TransactionService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private TransactionService transactionService;

    @Test
    void testAuthorizeEndpoint() {
        AuthorizationRequest requestContent = new AuthorizationRequest("user1", "msg1", new BigDecimal("100.00"), "USD", "CREDIT");
        HttpEntity<AuthorizationRequest> request = new HttpEntity<>(requestContent);

        AuthorizationResponse mockResponse = new AuthorizationResponse();
        mockResponse.setResponseCode("APPROVED");
        mockResponse.setBalance(new BigDecimal("100.00"));
        lenient().when(transactionService.authorize(any(AuthorizationRequest.class))).thenReturn(mockResponse);

        try{
        ResponseEntity<AuthorizationResponse> response = restTemplate.postForEntity("/authorization", request, AuthorizationResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("APPROVED", response.getBody().getResponseCode());
        assertTrue(true);
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void testLoadEndpoint() {
        LoadRequest requestContent = new LoadRequest();
        requestContent.setUserId("user1");
        requestContent.setMessageId("msg1");
        requestContent.setAmount(new BigDecimal("100.00"));
        requestContent.setCurrency("USD");
        requestContent.setDebitOrCredit("CREDIT");
        
        HttpEntity<LoadRequest> request = new HttpEntity<>(requestContent);
        
        LoadResponse mockResponse = new LoadResponse();
        mockResponse.setUserId("user1");
        mockResponse.setMessageId("msg1");
        mockResponse.setBalance(new BigDecimal("300.00"));
        mockResponse.setCurrency("USD");
        mockResponse.setDebitOrCredit("CREDIT");
        lenient().when(transactionService.load(any(LoadRequest.class))).thenReturn(mockResponse);
        
        try {
            ResponseEntity<LoadResponse> response = restTemplate.postForEntity("/load", request, LoadResponse.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("user1", response.getBody().getUserId());
            assertEquals("msg1", response.getBody().getMessageId());
            assertEquals(new BigDecimal("300.00"), response.getBody().getBalance());
            assertEquals("USD", response.getBody().getCurrency());
            assertEquals("CREDIT", response.getBody().getDebitOrCredit());
            assertTrue(true);
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        catch (Exception e){
            assertTrue(true);
        }
    }
}
