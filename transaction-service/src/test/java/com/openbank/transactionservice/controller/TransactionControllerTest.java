package com.openbank.transactionservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openbank.transactionservice.dto.*;
import com.openbank.transactionservice.entity.TransactionStatus;
import com.openbank.transactionservice.entity.TransactionType;
import com.openbank.transactionservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for TransactionController.
 * Tests all REST endpoints for proper request handling, validation, and response formatting.
 *
 * @author OpenBank Development Team
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(TransactionController.class)
@WithMockUser(username = "testuser", roles = {"USER", "ADMIN"})
@DisplayName("Transaction Controller Tests")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    private ObjectMapper objectMapper;

    // Test data
    private CreateTransactionRequest createTransactionRequest;
    private TransactionResponse transactionResponse;
    private TransactionSummaryResponse transactionSummaryResponse;
    private PagedResponse<TransactionResponse> pagedTransactionResponse;
    private PagedResponse<TransactionSummaryResponse> pagedSummaryResponse;
    private TransactionStatisticsResponse statisticsResponse;
    private AccountTransactionStatisticsResponse accountStatisticsResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        setupTestData();
    }

    private void setupTestData() {
        // Create Transaction Request
        createTransactionRequest = CreateTransactionRequest.builder()
                .transactionType("TRANSFER")
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .description("Test transfer")
                .build();

        // Transaction Response
        transactionResponse = TransactionResponse.builder()
                .id(1L)
                .reference("TXN-001")
                .transactionType("TRANSFER")
                .fromAccountId(1L)
                .toAccountId(2L)
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .fee(new BigDecimal("1.00"))
                .status("COMPLETED")
                .description("Test transfer")
                .createdAt(LocalDateTime.now())
                .processedAt(LocalDateTime.now())
                .build();

        // Transaction Summary Response
        transactionSummaryResponse = TransactionSummaryResponse.builder()
                .id(1L)
                .reference("TXN-001")
                .transactionType("TRANSFER")
                .amount(new BigDecimal("100.00"))
                .status("COMPLETED")
                .description("Test transfer")
                .otherAccountId(2L)
                .direction("OUTGOING")
                .createdAt(LocalDateTime.now())
                .build();

        // Paged Transaction Response
        pagedTransactionResponse = PagedResponse.<TransactionResponse>builder()
                .content(Arrays.asList(transactionResponse))
                .page(0)
                .size(20)
                .totalElements(1L)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        // Paged Summary Response
        pagedSummaryResponse = PagedResponse.<TransactionSummaryResponse>builder()
                .content(Arrays.asList(transactionSummaryResponse))
                .page(0)
                .size(20)
                .totalElements(1L)
                .totalPages(1)
                .first(true)
                .last(true)
                .build();

        // Statistics Response
        statisticsResponse = TransactionStatisticsResponse.builder()
                .totalTransactions(100L)
                .completedTransactions(90L)
                .pendingTransactions(5L)
                .failedTransactions(5L)
                .successRate(90.0)
                .build();

        // Account Statistics Response
        accountStatisticsResponse = AccountTransactionStatisticsResponse.builder()
                .accountId(1L)
                .totalTransactions(50L)
                .netAmountThisMonth(new BigDecimal("2000.00"))
                .build();
    }

    @Test
    @DisplayName("Health check should return OK")
    void healthCheck_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/transactions/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction Service is running"));
    }

    @Test
    @DisplayName("Create transaction should return created transaction")
    void createTransaction_ShouldReturnCreatedTransaction() throws Exception {
        when(transactionService.createTransaction(any(CreateTransactionRequest.class)))
                .thenReturn(transactionResponse);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTransactionRequest))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.reference").value("TXN-001"))
                .andExpect(jsonPath("$.transactionType").value("TRANSFER"))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @DisplayName("Create transaction with invalid data should return bad request")
    void createTransaction_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CreateTransactionRequest invalidRequest = CreateTransactionRequest.builder()
                .transactionType("TRANSFER")
                .amount(new BigDecimal("-100.00")) // Invalid negative amount
                .currency("INVALID") // Invalid currency
                .build();

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get transaction by ID should return transaction")
    void getTransactionById_ShouldReturnTransaction() throws Exception {
        when(transactionService.getTransactionById(1L)).thenReturn(transactionResponse);

        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.reference").value("TXN-001"));
    }

    @Test
    @DisplayName("Get transaction by reference should return transaction")
    void getTransactionByReference_ShouldReturnTransaction() throws Exception {
        when(transactionService.getTransactionByReference("TXN-001")).thenReturn(transactionResponse);

        mockMvc.perform(get("/api/transactions/reference/TXN-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.reference").value("TXN-001"));
    }

    @Test
    @DisplayName("Get all transactions should return paged response")
    void getAllTransactions_ShouldReturnPagedResponse() throws Exception {
        when(transactionService.getAllTransactions(any(Pageable.class)))
                .thenReturn(pagedTransactionResponse);

        mockMvc.perform(get("/api/transactions")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sortBy", "createdAt")
                        .param("sortDir", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Get transactions by account ID should return transaction list")
    void getTransactionsByAccountId_ShouldReturnTransactionList() throws Exception {
        when(transactionService.getTransactionsByAccountId(1L))
                .thenReturn(Arrays.asList(transactionSummaryResponse));

        mockMvc.perform(get("/api/transactions/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].reference").value("TXN-001"));
    }

    @Test
    @DisplayName("Get transactions by account ID with pagination should return paged response")
    void getTransactionsByAccountIdPaged_ShouldReturnPagedResponse() throws Exception {
        when(transactionService.getTransactionsByAccountIdPaged(eq(1L), any(Pageable.class)))
                .thenReturn(pagedSummaryResponse);

        mockMvc.perform(get("/api/transactions/account/1/paged")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sortBy", "createdAt")
                        .param("sortDir", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20));
    }

    @Test
    @DisplayName("Get transactions by status should return paged response")
    void getTransactionsByStatus_ShouldReturnPagedResponse() throws Exception {
        when(transactionService.getTransactionsByStatus(eq(TransactionStatus.COMPLETED), any(Pageable.class)))
                .thenReturn(pagedTransactionResponse);

        mockMvc.perform(get("/api/transactions/status/COMPLETED")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].status").value("COMPLETED"));
    }

    @Test
    @DisplayName("Get transactions by invalid status should return bad request")
    void getTransactionsByStatus_WithInvalidStatus_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/transactions/status/INVALID_STATUS"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get transactions by type should return paged response")
    void getTransactionsByType_ShouldReturnPagedResponse() throws Exception {
        when(transactionService.getTransactionsByType(eq(TransactionType.TRANSFER), any(Pageable.class)))
                .thenReturn(pagedTransactionResponse);

        mockMvc.perform(get("/api/transactions/type/TRANSFER")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].transactionType").value("TRANSFER"));
    }

    @Test
    @DisplayName("Get transactions by invalid type should return bad request")
    void getTransactionsByType_WithInvalidType_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/transactions/type/INVALID_TYPE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get transactions by date range should return transaction list")
    void getTransactionsByDateRange_ShouldReturnTransactionList() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        
        when(transactionService.getTransactionsByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(transactionResponse));

        mockMvc.perform(get("/api/transactions/date-range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Get transactions by date range with invalid date format should return bad request")
    void getTransactionsByDateRange_WithInvalidDateFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/transactions/date-range")
                        .param("startDate", "invalid-date")
                        .param("endDate", "invalid-date"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get account transactions by date range should return transaction list")
    void getAccountTransactionsByDateRange_ShouldReturnTransactionList() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        
        when(transactionService.getAccountTransactionsByDateRange(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(transactionSummaryResponse));

        mockMvc.perform(get("/api/transactions/account/1/date-range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Cancel transaction should return cancelled transaction")
    void cancelTransaction_ShouldReturnCancelledTransaction() throws Exception {
        TransactionResponse cancelledResponse = TransactionResponse.builder()
                .id(1L)
                .reference("TXN-001")
                .status("CANCELLED")
                .build();

        when(transactionService.cancelTransaction(1L)).thenReturn(cancelledResponse);

        mockMvc.perform(post("/api/transactions/1/cancel")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    @DisplayName("Retry transaction should return retried transaction")
    void retryTransaction_ShouldReturnRetriedTransaction() throws Exception {
        TransactionResponse retriedResponse = TransactionResponse.builder()
                .id(1L)
                .reference("TXN-001")
                .status("PENDING")
                .build();

        when(transactionService.retryTransaction(1L)).thenReturn(retriedResponse);

        mockMvc.perform(post("/api/transactions/1/retry")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Get transaction statistics should return statistics")
    void getTransactionStatistics_ShouldReturnStatistics() throws Exception {
        when(transactionService.getTransactionStatistics()).thenReturn(statisticsResponse);

        mockMvc.perform(get("/api/transactions/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTransactions").value(100))
                .andExpect(jsonPath("$.completedTransactions").value(90))
                .andExpect(jsonPath("$.pendingTransactions").value(5))
                .andExpect(jsonPath("$.failedTransactions").value(5))
                .andExpect(jsonPath("$.successRate").value(90.0));
    }

    @Test
    @DisplayName("Get account transaction statistics should return account statistics")
    void getAccountTransactionStatistics_ShouldReturnAccountStatistics() throws Exception {
        when(transactionService.getAccountTransactionStatistics(1L)).thenReturn(accountStatisticsResponse);

        mockMvc.perform(get("/api/transactions/account/1/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.totalTransactions").value(50))
                .andExpect(jsonPath("$.netAmountThisMonth").value(2000.00));
    }

    @Test
    @DisplayName("Process pending transactions should return success message")
    void processPendingTransactions_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(post("/api/transactions/admin/process-pending")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Pending transactions processed successfully"));
    }

    @Test
    @DisplayName("Cleanup old pending transactions should return success message")
    void cleanupOldPendingTransactions_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(post("/api/transactions/admin/cleanup-old-pending")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Old pending transactions cleaned up successfully"));
    }

    @Test
    @DisplayName("Get all transactions with custom sorting should work")
    void getAllTransactions_WithCustomSorting_ShouldWork() throws Exception {
        Sort expectedSort = Sort.by(Sort.Direction.ASC, "amount");
        Pageable expectedPageable = PageRequest.of(1, 10, expectedSort);
        
        when(transactionService.getAllTransactions(eq(expectedPageable)))
                .thenReturn(pagedTransactionResponse);

        mockMvc.perform(get("/api/transactions")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sortBy", "amount")
                        .param("sortDir", "ASC"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get transactions by status with custom sorting should work")
    void getTransactionsByStatus_WithCustomSorting_ShouldWork() throws Exception {
        Sort expectedSort = Sort.by(Sort.Direction.ASC, "amount");
        Pageable expectedPageable = PageRequest.of(1, 10, expectedSort);
        
        when(transactionService.getTransactionsByStatus(eq(TransactionStatus.PENDING), eq(expectedPageable)))
                .thenReturn(pagedTransactionResponse);

        mockMvc.perform(get("/api/transactions/status/PENDING")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sortBy", "amount")
                        .param("sortDir", "ASC"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get transactions by type with custom sorting should work")
    void getTransactionsByType_WithCustomSorting_ShouldWork() throws Exception {
        Sort expectedSort = Sort.by(Sort.Direction.ASC, "amount");
        Pageable expectedPageable = PageRequest.of(1, 10, expectedSort);
        
        when(transactionService.getTransactionsByType(eq(TransactionType.DEPOSIT), eq(expectedPageable)))
                .thenReturn(pagedTransactionResponse);

        mockMvc.perform(get("/api/transactions/type/DEPOSIT")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sortBy", "amount")
                        .param("sortDir", "ASC"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("All endpoints should handle missing path variables correctly")
    void endpointsWithPathVariables_ShouldHandleMissingVariables() throws Exception {
        // Test path without ID should hit the getAllTransactions endpoint
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk()); // This should hit the getAllTransactions endpoint

        // Test missing reference
        mockMvc.perform(get("/api/transactions/reference/"))
                .andExpect(status().isNotFound()); // Missing path variable
    }

    @Test
    @DisplayName("Endpoints should handle invalid path variable types")
    void endpointsWithPathVariables_ShouldHandleInvalidTypes() throws Exception {
        // Test invalid transaction ID (non-numeric)
        mockMvc.perform(get("/api/transactions/invalid-id"))
                .andExpect(status().isBadRequest());

        // Test invalid account ID (non-numeric)
        mockMvc.perform(get("/api/transactions/account/invalid-id"))
                .andExpect(status().isBadRequest());
    }
}
