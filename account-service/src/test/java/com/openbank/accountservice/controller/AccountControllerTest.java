package com.openbank.accountservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openbank.accountservice.dto.CreateAccountRequest;
import com.openbank.accountservice.dto.AccountResponse;
import com.openbank.accountservice.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
class AccountControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AccountService accountService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void healthCheck_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/accounts/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account Service is running"));
    }
    
    @Test
    void createAccount_ShouldReturnCreated_WhenValidRequest() throws Exception {
        // Given
        CreateAccountRequest request = CreateAccountRequest.builder()
                .userId(1L)
                .accountType("SAVINGS")
                .initialCredit(BigDecimal.valueOf(100))
                .currency("USD")
                .build();
        
        AccountResponse response = AccountResponse.builder()
                .id(1L)
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0001")
                .accountType("SAVINGS")
                .balance(BigDecimal.valueOf(100))
                .status("ACTIVE")
                .currency("USD")
                .build();
        
        when(accountService.createAccount(any(CreateAccountRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.balance").value(100))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
    
    @Test
    void createAccount_ShouldReturnBadRequest_WhenUserIdIsNull() throws Exception {
        // Given
        CreateAccountRequest request = CreateAccountRequest.builder()
                .accountType("SAVINGS")
                .initialCredit(BigDecimal.valueOf(100))
                .currency("USD")
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void getAccount_ShouldReturnAccount_WhenAccountExists() throws Exception {
        // Given
        AccountResponse response = AccountResponse.builder()
                .id(1L)
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0001")
                .accountType("SAVINGS")
                .balance(BigDecimal.valueOf(1000))
                .status("ACTIVE")
                .currency("USD")
                .build();
        
        when(accountService.getAccountById(1L)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
    
    @Test
    void getAccountBalance_ShouldReturnBalance_WhenAccountExists() throws Exception {
        // Given
        when(accountService.getAccountBalance(1L)).thenReturn(BigDecimal.valueOf(1500.50));
        
        // When & Then
        mockMvc.perform(get("/api/accounts/1/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500.5"));
    }
}
