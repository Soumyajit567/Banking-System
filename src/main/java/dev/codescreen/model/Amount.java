package dev.codescreen.model;

public class Amount {
    private String amount;
    private String currency;
    private String debitOrCredit;
    
    public Amount(String amount, String currency, String debitOrCredit) {
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
    }
    

    public String getAmount() {
        return amount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public String getDebitOrCredit() {
        return debitOrCredit;
    }
}