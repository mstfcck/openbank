package com.openbank.transactionservice.entity;

import com.openbank.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing a financial transaction in the banking system.
 * Supports various transaction types including deposits, withdrawals, transfers, and payments.
 * 
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_transaction_from_account", columnList = "from_account_id"),
    @Index(name = "idx_transaction_to_account", columnList = "to_account_id"),
    @Index(name = "idx_transaction_reference", columnList = "reference", unique = true),
    @Index(name = "idx_transaction_status", columnList = "status"),
    @Index(name = "idx_transaction_type", columnList = "transaction_type"),
    @Index(name = "idx_transaction_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {
    
    /**
     * Source account ID for the transaction.
     * Null for deposits from external sources.
     */
    @Column(name = "from_account_id")
    private Long fromAccountId;
    
    /**
     * Destination account ID for the transaction.
     * Null for withdrawals to external destinations.
     */
    @Column(name = "to_account_id")
    private Long toAccountId;
    
    /**
     * Transaction amount. Must be positive.
     */
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than 0")
    @Digits(integer = 17, fraction = 2, message = "Transaction amount must have at most 2 decimal places")
    private BigDecimal amount;
    
    /**
     * Type of transaction (DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, REFUND)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;
    
    /**
     * Current status of the transaction
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "Transaction status is required")
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;
    
    /**
     * Human-readable description of the transaction
     */
    @Column(name = "description", length = 500)
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    /**
     * Unique reference number for tracking the transaction
     */
    @Column(name = "reference", nullable = false, unique = true, length = 50)
    @NotBlank(message = "Transaction reference is required")
    @Size(max = 50, message = "Reference cannot exceed 50 characters")
    private String reference;
    
    /**
     * Currency code for the transaction (e.g., USD, EUR, GBP)
     */
    @Column(name = "currency", nullable = false, length = 3)
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter uppercase code")
    @Builder.Default
    private String currency = "USD";
    
    /**
     * Optional fee charged for this transaction
     */
    @Column(name = "fee", precision = 19, scale = 2)
    @DecimalMin(value = "0.00", message = "Fee cannot be negative")
    @Digits(integer = 17, fraction = 2, message = "Fee must have at most 2 decimal places")
    @Builder.Default
    private BigDecimal fee = BigDecimal.ZERO;
    
    /**
     * Timestamp when the transaction was processed/completed
     */
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
    
    /**
     * Error message if transaction failed
     */
    @Column(name = "error_message", length = 1000)
    @Size(max = 1000, message = "Error message cannot exceed 1000 characters")
    private String errorMessage;
    
    /**
     * Validates if this is a valid transfer transaction
     */
    public boolean isValidTransfer() {
        return transactionType == TransactionType.TRANSFER 
               && fromAccountId != null 
               && toAccountId != null 
               && !fromAccountId.equals(toAccountId);
    }
    
    /**
     * Validates if this is a valid deposit transaction
     */
    public boolean isValidDeposit() {
        return transactionType == TransactionType.DEPOSIT 
               && toAccountId != null 
               && fromAccountId == null;
    }
    
    /**
     * Validates if this is a valid withdrawal transaction
     */
    public boolean isValidWithdrawal() {
        return transactionType == TransactionType.WITHDRAWAL 
               && fromAccountId != null 
               && toAccountId == null;
    }
    
    /**
     * Validates if this is a valid payment transaction
     */
    public boolean isValidPayment() {
        return transactionType == TransactionType.PAYMENT 
               && fromAccountId != null;
    }
    
    /**
     * Marks the transaction as completed
     */
    public void markAsCompleted() {
        this.status = TransactionStatus.COMPLETED;
        this.processedAt = LocalDateTime.now();
        this.errorMessage = null;
    }
    
    /**
     * Marks the transaction as failed with an error message
     */
    public void markAsFailed(String errorMessage) {
        this.status = TransactionStatus.FAILED;
        this.processedAt = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }
    
    /**
     * Marks the transaction as cancelled
     */
    public void markAsCancelled() {
        this.status = TransactionStatus.CANCELLED;
        this.processedAt = LocalDateTime.now();
    }
    
    /**
     * Marks the transaction as processing
     */
    public void markAsProcessing() {
        this.status = TransactionStatus.PROCESSING;
    }
    
    /**
     * Checks if the transaction is in a final state (completed, failed, cancelled, reversed)
     */
    public boolean isFinalState() {
        return status == TransactionStatus.COMPLETED 
               || status == TransactionStatus.FAILED 
               || status == TransactionStatus.CANCELLED 
               || status == TransactionStatus.REVERSED;
    }
    
    /**
     * Gets the total amount including fees
     */
    public BigDecimal getTotalAmount() {
        return amount.add(fee);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(getId(), that.getId()) && 
               Objects.equals(reference, that.reference);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getId(), reference);
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + getId() +
                ", reference='" + reference + '\'' +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", status=" + status +
                ", fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}
