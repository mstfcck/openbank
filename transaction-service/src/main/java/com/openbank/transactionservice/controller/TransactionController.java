package com.openbank.transactionservice.controller;

import com.openbank.transactionservice.dto.*;
import com.openbank.transactionservice.entity.TransactionStatus;
import com.openbank.transactionservice.entity.TransactionType;
import com.openbank.transactionservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for transaction operations.
 * Provides endpoints for creating, querying, and managing transactions.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Transaction Management", description = "API for managing financial transactions")
@SecurityRequirement(name = "basicAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Health check endpoint")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy",
                content = @Content(schema = @Schema(type = "string")))
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check endpoint called");
        return ResponseEntity.ok("Transaction Service is running");
    }

    @Operation(summary = "Create a new transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "409", description = "Transaction already exists")
    })
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {
        log.info("Creating transaction: {} {} from account {} to account {}",
                request.getTransactionType(), request.getAmount(),
                request.getFromAccountId(), request.getToAccountId());
        TransactionResponse response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get transaction by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction found",
                content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long id) {
        log.info("Fetching transaction with ID: {}", id);
        TransactionResponse response = transactionService.getTransactionById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transaction by reference")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction found",
                content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/reference/{reference}")
    public ResponseEntity<TransactionResponse> getTransactionByReference(
            @Parameter(description = "Transaction reference", required = true)
            @PathVariable String reference) {
        log.info("Fetching transaction with reference: {}", reference);
        TransactionResponse response = transactionService.getTransactionByReference(reference);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all transactions with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<PagedResponse<TransactionResponse>> getAllTransactions(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDir) {

        log.info("Fetching all transactions with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        PagedResponse<TransactionResponse> response = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transactions by account ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionSummaryResponse>> getTransactionsByAccountId(
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long accountId) {
        log.info("Fetching transactions for account ID: {}", accountId);
        List<TransactionSummaryResponse> response = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transactions by account ID with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/account/{accountId}/paged")
    public ResponseEntity<PagedResponse<TransactionSummaryResponse>> getTransactionsByAccountIdPaged(
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long accountId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDir) {

        log.info("Fetching transactions for account ID: {} with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}",
                accountId, page, size, sortBy, sortDir);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        PagedResponse<TransactionSummaryResponse> response = transactionService.getTransactionsByAccountIdPaged(accountId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transactions by status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid status")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<PagedResponse<TransactionResponse>> getTransactionsByStatus(
            @Parameter(description = "Transaction status", required = true)
            @PathVariable String status,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDir) {

        log.info("Fetching transactions with status: {} with pagination", status);

        try {
            TransactionStatus transactionStatus = TransactionStatus.valueOf(status.toUpperCase());
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            PagedResponse<TransactionResponse> response = transactionService.getTransactionsByStatus(transactionStatus, pageable);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid transaction status: {}", status);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get transactions by type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid type")
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<PagedResponse<TransactionResponse>> getTransactionsByType(
            @Parameter(description = "Transaction type", required = true)
            @PathVariable String type,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDir) {

        log.info("Fetching transactions with type: {} with pagination", type);

        try {
            TransactionType transactionType = TransactionType.valueOf(type.toUpperCase());
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            PagedResponse<TransactionResponse> response = transactionService.getTransactionsByType(transactionType, pageable);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid transaction type: {}", type);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get transactions within date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date format")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("Fetching transactions between {} and {}", startDate, endDate);
        List<TransactionResponse> response = transactionService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get account transactions within date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date format"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/account/{accountId}/date-range")
    public ResponseEntity<List<TransactionSummaryResponse>> getAccountTransactionsByDateRange(
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long accountId,
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("Fetching transactions for account {} between {} and {}", accountId, startDate, endDate);
        List<TransactionSummaryResponse> response = transactionService.getAccountTransactionsByDateRange(accountId, startDate, endDate);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancel a pending transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction cancelled successfully",
                content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Transaction cannot be cancelled"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<TransactionResponse> cancelTransaction(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long id) {
        log.info("Cancelling transaction with ID: {}", id);
        TransactionResponse response = transactionService.cancelTransaction(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retry a failed transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction retry initiated",
                content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Transaction cannot be retried"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PostMapping("/{id}/retry")
    public ResponseEntity<TransactionResponse> retryTransaction(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long id) {
        log.info("Retrying transaction with ID: {}", id);
        TransactionResponse response = transactionService.retryTransaction(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get overall transaction statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                content = @Content(schema = @Schema(implementation = TransactionStatisticsResponse.class)))
    })
    @GetMapping("/statistics")
    public ResponseEntity<TransactionStatisticsResponse> getTransactionStatistics() {
        log.info("Fetching transaction statistics");
        TransactionStatisticsResponse response = transactionService.getTransactionStatistics();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get account-specific transaction statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                content = @Content(schema = @Schema(implementation = AccountTransactionStatisticsResponse.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/account/{accountId}/statistics")
    public ResponseEntity<AccountTransactionStatisticsResponse> getAccountTransactionStatistics(
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long accountId) {
        log.info("Fetching transaction statistics for account: {}", accountId);
        AccountTransactionStatisticsResponse response = transactionService.getAccountTransactionStatistics(accountId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Process pending transactions (admin endpoint)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pending transactions processed")
    })
    @PostMapping("/admin/process-pending")
    public ResponseEntity<String> processPendingTransactions() {
        log.info("Processing pending transactions - admin endpoint called");
        transactionService.processPendingTransactions();
        return ResponseEntity.ok("Pending transactions processed successfully");
    }

    @Operation(summary = "Clean up old pending transactions (admin endpoint)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Old pending transactions cleaned up")
    })
    @PostMapping("/admin/cleanup-old-pending")
    public ResponseEntity<String> cleanupOldPendingTransactions() {
        log.info("Cleaning up old pending transactions - admin endpoint called");
        transactionService.cleanupOldPendingTransactions();
        return ResponseEntity.ok("Old pending transactions cleaned up successfully");
    }
}
