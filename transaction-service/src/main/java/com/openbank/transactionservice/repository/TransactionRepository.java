package com.openbank.transactionservice.repository;

import com.openbank.transactionservice.entity.Transaction;
import com.openbank.transactionservice.entity.TransactionStatus;
import com.openbank.transactionservice.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Transaction entity.
 * Provides data access methods for transaction operations.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find transaction by unique reference number
     */
    Optional<Transaction> findByReference(String reference);

    /**
     * Check if transaction exists by reference
     */
    boolean existsByReference(String reference);

    /**
     * Find all transactions for a specific account (either source or destination)
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromAccountId = :accountId OR t.toAccountId = :accountId ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId);

    /**
     * Find transactions for a specific account with pagination
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromAccountId = :accountId OR t.toAccountId = :accountId ORDER BY t.createdAt DESC")
    Page<Transaction> findByAccountId(@Param("accountId") Long accountId, Pageable pageable);

    /**
     * Find transactions where account is the source
     */
    List<Transaction> findByFromAccountIdOrderByCreatedAtDesc(Long fromAccountId);

    /**
     * Find transactions where account is the destination
     */
    List<Transaction> findByToAccountIdOrderByCreatedAtDesc(Long toAccountId);

    /**
     * Find transactions by status
     */
    List<Transaction> findByStatusOrderByCreatedAtDesc(TransactionStatus status);

    /**
     * Find transactions by status with pagination
     */
    Page<Transaction> findByStatus(TransactionStatus status, Pageable pageable);

    /**
     * Find transactions by type
     */
    List<Transaction> findByTransactionTypeOrderByCreatedAtDesc(TransactionType transactionType);

    /**
     * Find transactions by type with pagination
     */
    Page<Transaction> findByTransactionType(TransactionType transactionType, Pageable pageable);

    /**
     * Find transactions by account and status
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccountId = :accountId OR t.toAccountId = :accountId) AND t.status = :status ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountIdAndStatus(@Param("accountId") Long accountId, @Param("status") TransactionStatus status);

    /**
     * Find transactions by account and type
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccountId = :accountId OR t.toAccountId = :accountId) AND t.transactionType = :type ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountIdAndType(@Param("accountId") Long accountId, @Param("type") TransactionType type);

    /**
     * Find transactions within a date range
     */
    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Find transactions for account within date range
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccountId = :accountId OR t.toAccountId = :accountId) AND t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountIdAndDateRange(@Param("accountId") Long accountId, 
                                                 @Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate);

    /**
     * Find transactions by amount range
     */
    @Query("SELECT t FROM Transaction t WHERE t.amount BETWEEN :minAmount AND :maxAmount ORDER BY t.createdAt DESC")
    List<Transaction> findByAmountRange(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);

    /**
     * Find pending transactions older than specified date (for cleanup/timeout)
     */
    @Query("SELECT t FROM Transaction t WHERE t.status = :status AND t.createdAt < :cutoffDate")
    List<Transaction> findPendingTransactionsOlderThan(@Param("status") TransactionStatus status, @Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Count transactions by status
     */
    long countByStatus(TransactionStatus status);

    /**
     * Count transactions for a specific account
     */
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.fromAccountId = :accountId OR t.toAccountId = :accountId")
    long countByAccountId(@Param("accountId") Long accountId);

    /**
     * Count transactions by type
     */
    long countByTransactionType(TransactionType transactionType);

    /**
     * Calculate total amount for account within date range
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN t.toAccountId = :accountId THEN t.amount ELSE -t.amount END), 0) " +
           "FROM Transaction t WHERE (t.fromAccountId = :accountId OR t.toAccountId = :accountId) " +
           "AND t.status = 'COMPLETED' AND t.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateNetAmountForAccountInDateRange(@Param("accountId") Long accountId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Find largest transactions for an account
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccountId = :accountId OR t.toAccountId = :accountId) " +
           "AND t.status = 'COMPLETED' ORDER BY t.amount DESC")
    List<Transaction> findLargestTransactionsForAccount(@Param("accountId") Long accountId, Pageable pageable);

    /**
     * Find failed transactions with error messages
     */
    @Query("SELECT t FROM Transaction t WHERE t.status = 'FAILED' AND t.errorMessage IS NOT NULL ORDER BY t.createdAt DESC")
    List<Transaction> findFailedTransactionsWithErrors();
}
