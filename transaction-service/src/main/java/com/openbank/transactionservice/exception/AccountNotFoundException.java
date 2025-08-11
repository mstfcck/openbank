package com.openbank.transactionservice.exception;

/**
 * Exception thrown when a requested account is not found.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
public class AccountNotFoundException extends RuntimeException {
    
    public AccountNotFoundException(String message) {
        super(message);
    }
    
    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AccountNotFoundException(Long accountId) {
        super("Account not found with id: " + accountId);
    }
}
