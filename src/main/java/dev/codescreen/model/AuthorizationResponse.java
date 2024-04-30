package dev.codescreen.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class AuthorizationResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private String messageId;
    private String responseCode;
    private BigDecimal balance;
    private String currency;
    private String debitOrCredit;
    private String errorMessage; 

    /** 
     * Default constructor for JPA
     */
    public AuthorizationResponse() {
    }

    /**
     * Full constructor including error message
     */
    public AuthorizationResponse(String userId, String messageId, String responseCode, BigDecimal balance, String currency, String debitOrCredit, String errorMessage) {
        this.userId = userId;
        this.messageId = messageId;
        this.responseCode = responseCode;
        this.balance = balance;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
        this.errorMessage = errorMessage;
    }

    /**
     * Getters and setters
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDebitOrCredit() {
        return debitOrCredit;
    }

    public void setDebitOrCredit(String debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
