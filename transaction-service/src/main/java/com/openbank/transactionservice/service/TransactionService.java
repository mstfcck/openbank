package com.openbank.transactionservice.service;

import com.openbank.transactionservice.client.AccountServiceClient;
import com.openbank.transactionservice.dto.*;
import com.openbank.transactionservice.entity.Transaction;
import com.openbank.transactionservice.entity.TransactionStatus;
import com.openbank.transactionservice.entity.TransactionType;
import com.openbank.transactionservice.exception.InvalidTransactionOperationException;
import com.openbank.transactionservice.exception.TransactionNotFoundException;
import com.openbank.transactionservice.repository.TransactionRepository;
import com.openbank.transactionservice.util.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for transaction operations.
 * Handles business logic for creating, processing, and querying transactions.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountServiceClient accountServiceClient;
    private final TransactionMapper transactionMapper;

    /**
     * Creates a new transaction
     */
    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        log.info("Creating transaction: {} {} from account {} to account {}",
                request.getTransactionType(), request.getAmount(),
                request.getFromAccountId(), request.getToAccountId());

        // Validate the transaction request
        validateTransactionRequest(request);

        // Create transaction entity
        Transaction transaction = transactionMapper.toEntity(request);

        // Validate and ensure accounts exist and can participate in the transaction
        validateTransactionAccounts(transaction);

        // Save transaction in PENDING state
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created with reference: {}", savedTransaction.getReference());

        // Process the transaction
        try {
            processTransaction(savedTransaction);
        } catch (Exception e) {
            log.error("Failed to process transaction {}: {}", savedTransaction.getReference(), e.getMessage());
            savedTransaction.markAsFailed(e.getMessage());
            transactionRepository.save(savedTransaction);
        }

        return transactionMapper.toResponse(savedTransaction);
    }

    /**
     * Get transaction by ID
     */
    @Transactional(readOnly = true)
    public Optional<TransactionResponse> getTransaction(Long id) {
        log.debug("Fetching transaction with ID: {}", id);
        return transactionRepository.findById(id)
                .map(transactionMapper::toResponse);
    }

    /**
     * Get transaction by ID (throws exception if not found)
     */
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) {
        log.debug("Fetching transaction with ID: {}", id);
        return transactionRepository.findById(id)
                .map(transactionMapper::toResponse)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    /**
     * Get transaction by reference
     */
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionByReference(String reference) {
        log.debug("Fetching transaction with reference: {}", reference);
        return transactionRepository.findByReference(reference)
                .map(transactionMapper::toResponse)
                .orElseThrow(() -> TransactionNotFoundException.byReference(reference));
    }

    /**
     * Get all transactions with pagination
     */
    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getAllTransactions(Pageable pageable) {
        log.debug("Fetching all transactions with pagination");
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        return transactionMapper.toPagedTransactionResponse(transactionPage);
    }

    /**
     * Get transactions for a specific account
     */
    @Transactional(readOnly = true)
    public List<TransactionSummaryResponse> getTransactionsByAccountId(Long accountId) {
        log.debug("Fetching transactions for account ID: {}", accountId);

        // Validate account exists
        accountServiceClient.getAccount(accountId);

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        return transactionMapper.toSummaryResponseList(transactions, accountId);
    }

    /**
     * Get transactions for a specific account with pagination
     */
    @Transactional(readOnly = true)
    public PagedResponse<TransactionSummaryResponse> getTransactionsByAccountIdPaged(Long accountId, Pageable pageable) {
        log.debug("Fetching transactions for account ID: {} with pagination", accountId);

        // Validate account exists
        accountServiceClient.getAccount(accountId);

        Page<Transaction> transactionPage = transactionRepository.findByAccountId(accountId, pageable);
        return transactionMapper.toPagedTransactionSummaryResponse(transactionPage, accountId);
    }

    /**
     * Get transactions by status
     */
    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getTransactionsByStatus(TransactionStatus status, Pageable pageable) {
        log.debug("Fetching transactions with status: {}", status);
        Page<Transaction> transactionPage = transactionRepository.findByStatus(status, pageable);
        return transactionMapper.toPagedTransactionResponse(transactionPage);
    }

    /**
     * Get transactions by type
     */
    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getTransactionsByType(TransactionType type, Pageable pageable) {
        log.debug("Fetching transactions with type: {}", type);
        Page<Transaction> transactionPage = transactionRepository.findByTransactionType(type, pageable);
        return transactionMapper.toPagedTransactionResponse(transactionPage);
    }

    /**
     * Get transactions within a date range
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching transactions between {} and {}", startDate, endDate);
        List<Transaction> transactions = transactionRepository.findByDateRange(startDate, endDate);
        return transactionMapper.toResponseList(transactions);
    }

    /**
     * Get transactions for account within date range
     */
    @Transactional(readOnly = true)
    public List<TransactionSummaryResponse> getAccountTransactionsByDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching transactions for account {} between {} and {}", accountId, startDate, endDate);

        // Validate account exists
        accountServiceClient.getAccount(accountId);

        List<Transaction> transactions = transactionRepository.findByAccountIdAndDateRange(accountId, startDate, endDate);
        return transactionMapper.toSummaryResponseList(transactions, accountId);
    }

    /**
     * Cancel a pending transaction
     */
    @Transactional
    public TransactionResponse cancelTransaction(Long transactionId) {
        log.info("Cancelling transaction with ID: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        if (transaction.isFinalState()) {
            throw new InvalidTransactionOperationException(
                    "Cannot cancel transaction in " + transaction.getStatus() + " state");
        }

        transaction.markAsCancelled();
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction cancelled: {}", savedTransaction.getReference());

        return transactionMapper.toResponse(savedTransaction);
    }

    /**
     * Retry a failed transaction
     */
    @Transactional
    public TransactionResponse retryTransaction(Long transactionId) {
        log.info("Retrying transaction with ID: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        if (transaction.getStatus() != TransactionStatus.FAILED) {
            throw new InvalidTransactionOperationException(
                    "Can only retry failed transactions. Current status: " + transaction.getStatus());
        }

        // Reset transaction state
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setErrorMessage(null);
        transaction.setProcessedAt(null);

        Transaction savedTransaction = transactionRepository.save(transaction);

        // Process the transaction
        try {
            processTransaction(savedTransaction);
        } catch (Exception e) {
            log.error("Failed to retry transaction {}: {}", savedTransaction.getReference(), e.getMessage());
            savedTransaction.markAsFailed(e.getMessage());
            transactionRepository.save(savedTransaction);
        }

        return transactionMapper.toResponse(savedTransaction);
    }

    /**
     * Get transaction statistics
     */
    @Transactional(readOnly = true)
    public TransactionStatisticsResponse getTransactionStatistics() {
        log.debug("Fetching transaction statistics");

        long totalTransactions = transactionRepository.count();
        long completedTransactions = transactionRepository.countByStatus(TransactionStatus.COMPLETED);
        long pendingTransactions = transactionRepository.countByStatus(TransactionStatus.PENDING);
        long failedTransactions = transactionRepository.countByStatus(TransactionStatus.FAILED);

        return TransactionStatisticsResponse.builder()
                .totalTransactions(totalTransactions)
                .completedTransactions(completedTransactions)
                .pendingTransactions(pendingTransactions)
                .failedTransactions(failedTransactions)
                .successRate(totalTransactions > 0 ? (double) completedTransactions / totalTransactions * 100 : 0.0)
                .build();
    }

    /**
     * Get account transaction statistics
     */
    @Transactional(readOnly = true)
    public AccountTransactionStatisticsResponse getAccountTransactionStatistics(Long accountId) {
        log.debug("Fetching transaction statistics for account: {}", accountId);

        // Validate account exists
        accountServiceClient.getAccount(accountId);

        long totalTransactions = transactionRepository.countByAccountId(accountId);
        
        // Calculate net amount for current month
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);
        
        BigDecimal netAmountThisMonth = transactionRepository.calculateNetAmountForAccountInDateRange(
                accountId, startOfMonth, endOfMonth);

        return AccountTransactionStatisticsResponse.builder()
                .accountId(accountId)
                .totalTransactions(totalTransactions)
                .netAmountThisMonth(netAmountThisMonth != null ? netAmountThisMonth : BigDecimal.ZERO)
                .build();
    }

    /**
     * Process pending transactions (for scheduled job)
     */
    @Transactional
    public void processPendingTransactions() {
        log.info("Processing pending transactions");
        
        List<Transaction> pendingTransactions = transactionRepository.findByStatusOrderByCreatedAtDesc(TransactionStatus.PENDING);
        
        for (Transaction transaction : pendingTransactions) {
            try {
                processTransaction(transaction);
            } catch (Exception e) {
                log.error("Failed to process pending transaction {}: {}", transaction.getReference(), e.getMessage());
                transaction.markAsFailed(e.getMessage());
                transactionRepository.save(transaction);
            }
        }
        
        log.info("Processed {} pending transactions", pendingTransactions.size());
    }

    /**
     * Clean up old pending transactions (for scheduled job)
     */
    @Transactional
    public void cleanupOldPendingTransactions() {
        log.info("Cleaning up old pending transactions");
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusHours(24); // 24 hours old
        List<Transaction> oldPendingTransactions = transactionRepository.findPendingTransactionsOlderThan(
                TransactionStatus.PENDING, cutoffDate);
        
        for (Transaction transaction : oldPendingTransactions) {
            transaction.markAsFailed("Transaction timeout - pending for more than 24 hours");
            transactionRepository.save(transaction);
            log.warn("Timed out old pending transaction: {}", transaction.getReference());
        }
        
        log.info("Cleaned up {} old pending transactions", oldPendingTransactions.size());
    }

    // Private helper methods

    private void validateTransactionRequest(CreateTransactionRequest request) {
        TransactionType type = transactionMapper.parseTransactionType(request.getTransactionType());

        switch (type) {
            case DEPOSIT:
                if (request.getToAccountId() == null) {
                    throw new InvalidTransactionOperationException("Deposit requires destination account");
                }
                if (request.getFromAccountId() != null) {
                    throw new InvalidTransactionOperationException("Deposit cannot have source account");
                }
                break;

            case WITHDRAWAL:
                if (request.getFromAccountId() == null) {
                    throw new InvalidTransactionOperationException("Withdrawal requires source account");
                }
                if (request.getToAccountId() != null) {
                    throw new InvalidTransactionOperationException("Withdrawal cannot have destination account");
                }
                break;

            case TRANSFER:
                if (request.getFromAccountId() == null || request.getToAccountId() == null) {
                    throw new InvalidTransactionOperationException("Transfer requires both source and destination accounts");
                }
                if (request.getFromAccountId().equals(request.getToAccountId())) {
                    throw new InvalidTransactionOperationException("Source and destination accounts cannot be the same");
                }
                break;

            case PAYMENT:
                if (request.getFromAccountId() == null) {
                    throw new InvalidTransactionOperationException("Payment requires source account");
                }
                break;

            case REFUND:
                if (request.getToAccountId() == null) {
                    throw new InvalidTransactionOperationException("Refund requires destination account");
                }
                break;

            default:
                throw new InvalidTransactionOperationException("Unsupported transaction type: " + type);
        }
    }

    private void validateTransactionAccounts(Transaction transaction) {
        // Validate source account if present
        if (transaction.getFromAccountId() != null) {
            if (!accountServiceClient.canDebit(transaction.getFromAccountId(), transaction.getTotalAmount())) {
                throw new InvalidTransactionOperationException("Source account cannot be debited");
            }
        }

        // Validate destination account if present
        if (transaction.getToAccountId() != null) {
            if (!accountServiceClient.canCredit(transaction.getToAccountId())) {
                throw new InvalidTransactionOperationException("Destination account cannot be credited");
            }
        }

        // For transfers, perform additional validation
        if (transaction.getTransactionType() == TransactionType.TRANSFER) {
            accountServiceClient.validateTransferAccounts(
                    transaction.getFromAccountId(),
                    transaction.getToAccountId(),
                    transaction.getTotalAmount()
            );
        }
    }

    private void processTransaction(Transaction transaction) {
        log.info("Processing transaction: {}", transaction.getReference());

        transaction.markAsProcessing();
        transactionRepository.save(transaction);

        try {
            switch (transaction.getTransactionType()) {
                case DEPOSIT:
                    processDeposit(transaction);
                    break;
                case WITHDRAWAL:
                    processWithdrawal(transaction);
                    break;
                case TRANSFER:
                    processTransfer(transaction);
                    break;
                case PAYMENT:
                    processPayment(transaction);
                    break;
                case REFUND:
                    processRefund(transaction);
                    break;
                default:
                    throw new InvalidTransactionOperationException("Unsupported transaction type: " + transaction.getTransactionType());
            }

            transaction.markAsCompleted();
            transactionRepository.save(transaction);
            log.info("Transaction completed successfully: {}", transaction.getReference());

        } catch (Exception e) {
            transaction.markAsFailed(e.getMessage());
            transactionRepository.save(transaction);
            throw e;
        }
    }

    private void processDeposit(Transaction transaction) {
        accountServiceClient.creditAccount(
                transaction.getToAccountId(),
                transaction.getAmount(),
                "Deposit - " + transaction.getDescription()
        );
    }

    private void processWithdrawal(Transaction transaction) {
        accountServiceClient.debitAccount(
                transaction.getFromAccountId(),
                transaction.getTotalAmount(),
                "Withdrawal - " + transaction.getDescription()
        );
    }

    private void processTransfer(Transaction transaction) {
        // Debit source account
        accountServiceClient.debitAccount(
                transaction.getFromAccountId(),
                transaction.getTotalAmount(),
                "Transfer to account " + transaction.getToAccountId() + " - " + transaction.getDescription()
        );

        // Credit destination account
        accountServiceClient.creditAccount(
                transaction.getToAccountId(),
                transaction.getAmount(),
                "Transfer from account " + transaction.getFromAccountId() + " - " + transaction.getDescription()
        );
    }

    private void processPayment(Transaction transaction) {
        accountServiceClient.debitAccount(
                transaction.getFromAccountId(),
                transaction.getTotalAmount(),
                "Payment - " + transaction.getDescription()
        );
    }

    private void processRefund(Transaction transaction) {
        accountServiceClient.creditAccount(
                transaction.getToAccountId(),
                transaction.getAmount(),
                "Refund - " + transaction.getDescription()
        );
    }
}
