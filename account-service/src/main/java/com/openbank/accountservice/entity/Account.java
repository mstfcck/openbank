package com.openbank.accountservice.entity;

import com.openbank.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts", indexes = {
    @Index(name = "idx_account_user_id", columnList = "user_id"),
    @Index(name = "idx_account_account_number", columnList = "account_number", unique = true),
    @Index(name = "idx_account_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Account extends BaseEntity {
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "account_number", nullable = false, unique = true, length = 30)
    private String accountNumber;
    
    @Column(name = "account_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccountType accountType = AccountType.SAVINGS;
    
    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
    
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;
    
    @Column(name = "currency", nullable = false, length = 3)
    @Builder.Default
    private String currency = "USD";
    
    @Column(name = "overdraft_limit", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal overdraftLimit = BigDecimal.ZERO;
    
    @Column(name = "opened_at", nullable = false, updatable = false)
    private LocalDateTime openedAt;
    
    @Column(name = "closed_at")
    private LocalDateTime closedAt;
    
    @PrePersist
    public void prePersist() {
        if (openedAt == null) {
            openedAt = LocalDateTime.now();
        }
    }
    
    public enum AccountType {
        CHECKING,
        SAVINGS,
        BUSINESS,
        INVESTMENT
    }
    
    public enum AccountStatus {
        ACTIVE,
        INACTIVE,
        CLOSED,
        FROZEN
    }
    
    // Business methods
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }
    
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        BigDecimal availableBalance = this.balance.add(this.overdraftLimit);
        if (amount.compareTo(availableBalance) > 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        this.balance = this.balance.subtract(amount);
    }
    
    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(this.status);
    }
    
    public void close() {
        this.status = AccountStatus.CLOSED;
        this.closedAt = LocalDateTime.now();
    }
}
