package com.openbank.accountservice.controller;

import com.openbank.accountservice.dto.*;
import com.openbank.accountservice.entity.Account;
import com.openbank.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account Management", description = "API for managing bank accounts")
public class AccountController {
    
    private final AccountService accountService;
    
    @Operation(summary = "Health check endpoint")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy",
                content = @Content(schema = @Schema(type = "string")))
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check endpoint called");
        return ResponseEntity.ok("Account Service is running");
    }
    
    @Operation(summary = "Create a new account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully",
                content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "Account already exists")
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        log.info("Creating account for user ID: {}", request.getUserId());
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Get account by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account found",
                content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long id) {
        log.info("Fetching account with ID: {}", id);
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get account by account number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account found",
                content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountByNumber(
            @Parameter(description = "Account number", required = true)
            @PathVariable String accountNumber) {
        log.info("Fetching account with number: {}", accountNumber);
        AccountResponse response = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        log.info("Fetching all accounts");
        List<AccountResponse> response = accountService.getAllAccounts();
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get all accounts with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully")
    })
    @GetMapping("/paged")
    public ResponseEntity<PagedResponse<AccountResponse>> getAllAccountsPaged(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        log.info("Fetching all accounts with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                page, size, sortBy, sortDir);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PagedResponse<AccountResponse> response = accountService.getAllAccountsPaged(pageable);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get accounts by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUserId(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {
        log.info("Fetching accounts for user ID: {}", userId);
        List<AccountResponse> response = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get accounts by user ID with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}/paged")
    public ResponseEntity<PagedResponse<AccountResponse>> getAccountsByUserIdPaged(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        log.info("Fetching accounts for user ID: {} with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                userId, page, size, sortBy, sortDir);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PagedResponse<AccountResponse> response = accountService.getAccountsByUserIdPaged(userId, pageable);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Update account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account updated successfully",
                content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequest request) {
        log.info("Updating account with ID: {}", id);
        AccountResponse response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Cannot delete account with non-zero balance"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long id) {
        log.info("Deleting account with ID: {}", id);
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Close account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account closed successfully",
                content = @Content(schema = @Schema(implementation = AccountResponse.class))),
        @ApiResponse(responseCode = "400", description = "Account is already closed"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PostMapping("/{id}/close")
    public ResponseEntity<AccountResponse> closeAccount(
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long id) {
        log.info("Closing account with ID: {}", id);
        AccountResponse response = accountService.closeAccount(id);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get account balance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(
            @Parameter(description = "Account ID", required = true)
            @PathVariable Long id) {
        log.info("Fetching balance for account ID: {}", id);
        BigDecimal balance = accountService.getAccountBalance(id);
        return ResponseEntity.ok(balance);
    }
    
    @Operation(summary = "Get account summaries for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account summaries retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}/summaries")
    public ResponseEntity<List<AccountSummaryResponse>> getAccountSummaries(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long userId) {
        log.info("Fetching account summaries for user ID: {}", userId);
        List<AccountSummaryResponse> response = accountService.getAccountSummaries(userId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get account statistics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    @GetMapping("/statistics")
    public ResponseEntity<AccountStatisticsResponse> getAccountStatistics() {
        log.info("Fetching account statistics");
        
        long totalAccounts = accountService.getAccountCount();
        long activeAccounts = accountService.getAccountCountByStatus(Account.AccountStatus.ACTIVE);
        long closedAccounts = accountService.getAccountCountByStatus(Account.AccountStatus.CLOSED);
        long frozenAccounts = accountService.getAccountCountByStatus(Account.AccountStatus.FROZEN);
        
        AccountStatisticsResponse response = AccountStatisticsResponse.builder()
                .totalAccounts(totalAccounts)
                .activeAccounts(activeAccounts)
                .closedAccounts(closedAccounts)
                .frozenAccounts(frozenAccounts)
                .build();
        
        return ResponseEntity.ok(response);
    }
}
