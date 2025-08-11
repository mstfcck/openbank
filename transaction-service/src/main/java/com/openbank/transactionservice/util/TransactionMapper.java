package com.openbank.transactionservice.util;

import com.openbank.transactionservice.dto.*;
import com.openbank.transactionservice.entity.Transaction;
import com.openbank.transactionservice.entity.TransactionStatus;
import com.openbank.transactionservice.entity.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Utility component for mapping between Transaction entities and DTOs.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@Component
public class TransactionMapper {

    /**
     * Maps a Transaction entity to TransactionResponse DTO
     */
    public TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .fromAccountId(transaction.getFromAccountId())
                .toAccountId(transaction.getToAccountId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType().name())
                .status(transaction.getStatus().name())
                .description(transaction.getDescription())
                .reference(transaction.getReference())
                .currency(transaction.getCurrency())
                .fee(transaction.getFee())
                .totalAmount(transaction.getTotalAmount())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .processedAt(transaction.getProcessedAt())
                .errorMessage(transaction.getErrorMessage())
                .createdBy(transaction.getCreatedBy())
                .lastModifiedBy(transaction.getUpdatedBy())
                .build();
    }

    /**
     * Maps a Transaction entity to TransactionSummaryResponse DTO for a specific account
     */
    public TransactionSummaryResponse toSummaryResponse(Transaction transaction, Long accountId) {
        TransactionSummaryResponse.TransactionSummaryResponseBuilder builder = TransactionSummaryResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType().name())
                .status(transaction.getStatus().name())
                .description(transaction.getDescription())
                .reference(transaction.getReference())
                .createdAt(transaction.getCreatedAt());

        // Determine direction and other account based on the perspective of the queried account
        if (transaction.getFromAccountId() != null && transaction.getFromAccountId().equals(accountId)) {
            // Money going out from this account
            builder.direction("OUTGOING");
            builder.otherAccountId(transaction.getToAccountId());
        } else if (transaction.getToAccountId() != null && transaction.getToAccountId().equals(accountId)) {
            // Money coming into this account
            builder.direction("INCOMING");
            builder.otherAccountId(transaction.getFromAccountId());
        } else {
            // This should not happen if the transaction is properly associated with the account
            builder.direction("UNKNOWN");
        }

        return builder.build();
    }

    /**
     * Maps a CreateTransactionRequest to Transaction entity
     */
    public Transaction toEntity(CreateTransactionRequest request) {
        return Transaction.builder()
                .fromAccountId(request.getFromAccountId())
                .toAccountId(request.getToAccountId())
                .amount(request.getAmount())
                .transactionType(parseTransactionType(request.getTransactionType()))
                .status(TransactionStatus.PENDING) // All new transactions start as PENDING
                .description(request.getDescription())
                .reference(generateTransactionReference(request.getExternalReference()))
                .currency(request.getCurrency())
                .fee(request.getFee())
                .build();
    }

    /**
     * Maps a list of Transaction entities to TransactionResponse DTOs
     */
    public List<TransactionResponse> toResponseList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Maps a list of Transaction entities to TransactionSummaryResponse DTOs for a specific account
     */
    public List<TransactionSummaryResponse> toSummaryResponseList(List<Transaction> transactions, Long accountId) {
        return transactions.stream()
                .map(transaction -> toSummaryResponse(transaction, accountId))
                .toList();
    }

    /**
     * Creates a paginated response from a Spring Data Page
     */
    public <T> PagedResponse<T> toPagedResponse(Page<T> page) {
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    /**
     * Maps a Page of Transaction entities to PagedResponse of TransactionResponse DTOs
     */
    public PagedResponse<TransactionResponse> toPagedTransactionResponse(Page<Transaction> page) {
        List<TransactionResponse> responses = page.getContent().stream()
                .map(this::toResponse)
                .toList();

        return PagedResponse.<TransactionResponse>builder()
                .content(responses)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    /**
     * Maps a Page of Transaction entities to PagedResponse of TransactionSummaryResponse DTOs for a specific account
     */
    public PagedResponse<TransactionSummaryResponse> toPagedTransactionSummaryResponse(Page<Transaction> page, Long accountId) {
        List<TransactionSummaryResponse> responses = page.getContent().stream()
                .map(transaction -> toSummaryResponse(transaction, accountId))
                .toList();

        return PagedResponse.<TransactionSummaryResponse>builder()
                .content(responses)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

    /**
     * Parses a transaction type string to TransactionType enum
     */
    public TransactionType parseTransactionType(String transactionType) {
        if (transactionType == null || transactionType.isEmpty()) {
            throw new IllegalArgumentException("Transaction type is required");
        }

        try {
            return TransactionType.valueOf(transactionType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }
    }

    /**
     * Parses a transaction status string to TransactionStatus enum
     */
    public TransactionStatus parseTransactionStatus(String status) {
        if (status == null || status.isEmpty()) {
            return TransactionStatus.PENDING;
        }

        try {
            return TransactionStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction status: " + status);
        }
    }

    /**
     * Generates a unique transaction reference
     */
    private String generateTransactionReference(String externalReference) {
        if (externalReference != null && !externalReference.trim().isEmpty()) {
            return "TXN-" + externalReference.trim();
        }
        
        // Generate a unique reference using timestamp and UUID
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        long timestamp = System.currentTimeMillis();
        return "TXN-" + timestamp + "-" + uuid;
    }
}
