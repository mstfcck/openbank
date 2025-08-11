package com.openbank.transactionservice.entity;

/**
 * Enumeration representing the status of a financial transaction.
 * 
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
public enum TransactionStatus {
    
    /**
     * Transaction has been initiated but not yet processed
     */
    PENDING("Pending"),
    
    /**
     * Transaction is currently being processed
     */
    PROCESSING("Processing"),
    
    /**
     * Transaction has been successfully completed
     */
    COMPLETED("Completed"),
    
    /**
     * Transaction failed due to insufficient funds, invalid account, or other business rules
     */
    FAILED("Failed"),
    
    /**
     * Transaction was cancelled by the user or system before completion
     */
    CANCELLED("Cancelled"),
    
    /**
     * Transaction was reversed due to error or fraud detection
     */
    REVERSED("Reversed");
    
    private final String displayName;
    
    TransactionStatus(String displayName) {
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
