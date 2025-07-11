package com.openbank.accountservice.exception;

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
