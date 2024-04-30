
package dev.codescreen.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class AuthorizationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "User ID is required")
    private String userId;

    @NotNull(message = "Message ID is required")
    private String messageId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Currency is required")
    private String currency;

    @NotNull(message = "Debit or Credit is required")
    @Pattern(regexp = "DEBIT|CREDIT", message = "Debit or Credit must be either 'DEBIT' or 'CREDIT'")
    private String debitOrCredit;

    /**
     * Default constructor for JPA
    */
    public AuthorizationRequest() {
    }

    /** 
     * Full constructor for easier testing and initialization
     */
    public AuthorizationRequest(String userId, String messageId, BigDecimal amount, String currency, String debitOrCredit) {
        this.userId = userId;
        this.messageId = messageId;
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
    }

    /**
     * Getters and setters
     */
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
}
