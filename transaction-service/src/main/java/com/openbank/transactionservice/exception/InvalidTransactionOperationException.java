package com.openbank.transactionservice.exception;

/**
 * Exception thrown when an invalid transaction operation is attempted.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
public class InvalidTransactionOperationException extends RuntimeException {
    
    public InvalidTransactionOperationException(String message) {
        super(message);
    }
    
    public InvalidTransactionOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
