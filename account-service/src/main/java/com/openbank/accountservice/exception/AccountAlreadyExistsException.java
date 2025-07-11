package com.openbank.accountservice.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
    
    public AccountAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AccountAlreadyExistsException(Long userId) {
        super("Account already exists for user with id: " + userId);
    }
}
