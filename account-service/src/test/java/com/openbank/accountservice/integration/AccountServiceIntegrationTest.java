package com.openbank.accountservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openbank.accountservice.dto.CreateAccountRequest;
import com.openbank.accountservice.entity.Account;
import com.openbank.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class AccountServiceIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private com.openbank.accountservice.client.UserServiceClient userServiceClient;
    
    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        // Mock user service to always return true for user existence
        when(userServiceClient.userExists(anyLong())).thenReturn(true);
    }
    
    @Test
    void createAccount_FullIntegration_ShouldCreateAccountSuccessfully() throws Exception {
        // Given
        CreateAccountRequest request = CreateAccountRequest.builder()
                .userId(1L)
                .accountType("SAVINGS")
                .initialCredit(BigDecimal.valueOf(500))
                .currency("USD")
                .overdraftLimit(BigDecimal.ZERO)
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"))
                .andExpect(jsonPath("$.balance").value(500))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.accountNumber").exists())
                .andExpect(jsonPath("$.accountNumber").value(startsWith("ACC-")));
        
        // Verify data was persisted
        assertThat(accountRepository.count()).isEqualTo(1L);
        Account savedAccount = accountRepository.findAll().get(0);
        assertThat(savedAccount.getUserId()).isEqualTo(1L);
        assertThat(savedAccount.getBalance()).isEqualTo(BigDecimal.valueOf(500));
        assertThat(savedAccount.getAccountType()).isEqualTo(Account.AccountType.SAVINGS);
    }
    
    @Test
    void getAccount_FullIntegration_ShouldReturnAccount() throws Exception {
        // Given - Create an account first
        Account account = Account.builder()
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0001")
                .accountType(Account.AccountType.CHECKING)
                .balance(BigDecimal.valueOf(1000))
                .status(Account.AccountStatus.ACTIVE)
                .currency("USD")
                .overdraftLimit(BigDecimal.valueOf(100))
                .openedAt(LocalDateTime.now())
                .build();
        
        Account savedAccount = accountRepository.save(account);
        
        // When & Then
        mockMvc.perform(get("/api/accounts/" + savedAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedAccount.getId()))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("ACC-20231201-0001-0001"))
                .andExpect(jsonPath("$.accountType").value("CHECKING"))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.overdraftLimit").value(100));
    }
    
    @Test
    void getAccountBalance_FullIntegration_ShouldReturnBalance() throws Exception {
        // Given
        Account account = Account.builder()
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0002")
                .accountType(Account.AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(1500.75))
                .status(Account.AccountStatus.ACTIVE)
                .currency("USD")
                .openedAt(LocalDateTime.now())
                .build();
        
        Account savedAccount = accountRepository.save(account);
        
        // When & Then
        mockMvc.perform(get("/api/accounts/" + savedAccount.getId() + "/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500.75"));
    }
    
    @Test
    void getUserAccounts_FullIntegration_ShouldReturnUserAccounts() throws Exception {
        // Given - Create multiple accounts for user
        Account account1 = Account.builder()
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0001")
                .accountType(Account.AccountType.CHECKING)
                .balance(BigDecimal.valueOf(1000))
                .status(Account.AccountStatus.ACTIVE)
                .currency("USD")
                .openedAt(LocalDateTime.now())
                .build();
        
        Account account2 = Account.builder()
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0002")
                .accountType(Account.AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(2000))
                .status(Account.AccountStatus.ACTIVE)
                .currency("USD")
                .openedAt(LocalDateTime.now())
                .build();
        
        // Account for different user (should not be returned)
        Account account3 = Account.builder()
                .userId(2L)
                .accountNumber("ACC-20231201-0002-0001")
                .accountType(Account.AccountType.CHECKING)
                .balance(BigDecimal.valueOf(500))
                .status(Account.AccountStatus.ACTIVE)
                .currency("USD")
                .openedAt(LocalDateTime.now())
                .build();
        
        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);
        
        // When & Then
        mockMvc.perform(get("/api/accounts/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].userId").value(everyItem(is(1))))
                .andExpect(jsonPath("$[*].accountNumber").value(containsInAnyOrder(
                        "ACC-20231201-0001-0001", "ACC-20231201-0001-0002")))
                .andExpect(jsonPath("$[*].accountType").value(containsInAnyOrder(
                        "CHECKING", "SAVINGS")));
    }
    
    @Test
    void createAccount_WithInvalidUserId_ShouldReturnBadRequest() throws Exception {
        // Given
        when(userServiceClient.userExists(999L)).thenReturn(false);
        
        CreateAccountRequest request = CreateAccountRequest.builder()
                .userId(999L)
                .accountType("SAVINGS")
                .initialCredit(BigDecimal.valueOf(100))
                .currency("USD")
                .build();
        
        // When & Then
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("User not found")));
        
        // Verify no account was created
        assertThat(accountRepository.count()).isZero();
    }
    
    @Test
    void getAccount_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/accounts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Account not found")));
    }
    
    @Test
    void deleteAccount_WithZeroBalance_ShouldDeleteSuccessfully() throws Exception {
        // Given
        Account account = Account.builder()
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0003")
                .accountType(Account.AccountType.CHECKING)
                .balance(BigDecimal.ZERO)
                .status(Account.AccountStatus.ACTIVE)
                .currency("USD")
                .openedAt(LocalDateTime.now())
                .build();
        
        Account savedAccount = accountRepository.save(account);
        
        // When & Then
        mockMvc.perform(delete("/api/accounts/" + savedAccount.getId()))
                .andExpect(status().isNoContent());
        
        // Verify account was deleted
        assertThat(accountRepository.findById(savedAccount.getId())).isEmpty();
    }
    
    @Test
    void deleteAccount_WithNonZeroBalance_ShouldReturnBadRequest() throws Exception {
        // Given
        Account account = Account.builder()
                .userId(1L)
                .accountNumber("ACC-20231201-0001-0004")
                .accountType(Account.AccountType.CHECKING)
                .balance(BigDecimal.valueOf(100))
                .status(Account.AccountStatus.ACTIVE)
                .currency("USD")
                .openedAt(LocalDateTime.now())
                .build();
        
        Account savedAccount = accountRepository.save(account);
        
        // When & Then
        mockMvc.perform(delete("/api/accounts/" + savedAccount.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("balance")));
        
        // Verify account was not deleted
        assertThat(accountRepository.findById(savedAccount.getId())).isPresent();
    }
    
    @Test
    void createAccount_ValidationErrors_ShouldReturnBadRequest() throws Exception {
        // Test with null userId
        CreateAccountRequest request = CreateAccountRequest.builder()
                .accountType("SAVINGS")
                .initialCredit(BigDecimal.valueOf(100))
                .currency("USD")
                .build();
        
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        
        // Test with invalid account type
        CreateAccountRequest invalidTypeRequest = CreateAccountRequest.builder()
                .userId(1L)
                .accountType("INVALID_TYPE")
                .initialCredit(BigDecimal.valueOf(100))
                .currency("USD")
                .build();
        
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTypeRequest)))
                .andExpect(status().isBadRequest());
        
        // Test with negative initial credit
        CreateAccountRequest negativeAmountRequest = CreateAccountRequest.builder()
                .userId(1L)
                .accountType("SAVINGS")
                .initialCredit(BigDecimal.valueOf(-100))
                .currency("USD")
                .build();
        
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(negativeAmountRequest)))
                .andExpect(status().isBadRequest());
    }
}
