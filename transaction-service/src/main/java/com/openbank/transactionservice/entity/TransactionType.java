package com.openbank.transactionservice.entity;

/**
 * Enumeration representing different types of financial transactions.
 * 
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
public enum TransactionType {
    
    /**
     * Money being added to an account from an external source
     */
    DEPOSIT("Deposit"),
    
    /**
     * Money being removed from an account to an external destination
     */
    WITHDRAWAL("Withdrawal"),
    
    /**
     * Money being moved from one account to another within the system
     */
    TRANSFER("Transfer"),
    
    /**
     * Payment made from an account to a merchant or service provider
     */
    PAYMENT("Payment"),
    
    /**
     * Refund of a previous transaction
     */
    REFUND("Refund");
    
    private final String displayName;
    
    TransactionType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
