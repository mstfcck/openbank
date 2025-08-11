package com.openbank.transactionservice.exception;

/**
 * Exception thrown when an external service call fails.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
public class ExternalServiceException extends RuntimeException {
    
    public ExternalServiceException(String message) {
        super(message);
    }
    
    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
