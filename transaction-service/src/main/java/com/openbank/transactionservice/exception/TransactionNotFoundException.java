package com.openbank.transactionservice.exception;

/**
 * Exception thrown when a requested transaction is not found.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
public class TransactionNotFoundException extends RuntimeException {
    
    public TransactionNotFoundException(String message) {
        super(message);
    }
    
    public TransactionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TransactionNotFoundException(Long transactionId) {
        super("Transaction not found with id: " + transactionId);
    }
    
    public static TransactionNotFoundException byReference(String reference) {
        return new TransactionNotFoundException("Transaction not found with reference: " + reference);
    }
}
